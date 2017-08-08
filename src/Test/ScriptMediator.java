/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   ScriptMediator.java

package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.activation.DataHandler;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
import org.apache.bsf.xml.XMLHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.config.Entry;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.Value;
import org.apache.synapse.mediators.bsf.ScriptMessageContext;
import org.mozilla.javascript.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.sun.phobos.script.javascript.RhinoScriptEngineFactory;
import com.sun.script.groovy.GroovyScriptEngineFactory;
import com.sun.script.jruby.JRubyScriptEngineFactory;
import com.sun.script.jython.JythonScriptEngineFactory;

// Referenced classes of package org.apache.synapse.mediators.bsf:
//            ScriptMessageContext

public class ScriptMediator extends AbstractMediator
{

    public ScriptMediator(String language, String scriptSourceCode, ClassLoader classLoader)
    {
        function = "mediate";
        resourceLock = new ReentrantLock();
        scriptEvaluatorLock = new ReentrantLock();
        this.language = language;
        this.scriptSourceCode = scriptSourceCode;
        setLoader(classLoader);
        includes = new TreeMap();
        initInlineScript();
    }

    public ScriptMediator(String language, Map includeKeysMap, Value key, String function, ClassLoader classLoader)
    {
        this.function = "mediate";
        resourceLock = new ReentrantLock();
        scriptEvaluatorLock = new ReentrantLock();
        this.language = language;
        this.key = key;
        setLoader(classLoader);
        includes = includeKeysMap;
        if(function != null)
            this.function = function;
        initScriptEngine();
        if(!(scriptEngine instanceof Invocable))
        {
            throw new SynapseException((new StringBuilder()).append("Script engine is not an Invocable engine for language: ").append(language).toString());
        } else
        {
            invocableScript = (Invocable)scriptEngine;
            return;
        }
    }

    public boolean mediate(MessageContext synCtx)
    {
        SynapseLog synLog = getLog(synCtx);
        if(synLog.isTraceOrDebugEnabled())
        {
            synLog.traceOrDebug("Start : Script mediator");
            if(synLog.isTraceTraceEnabled())
                synLog.traceTrace((new StringBuilder()).append("Message : ").append(synCtx.getEnvelope()).toString());
        }
        if(synLog.isTraceOrDebugEnabled())
            synLog.traceOrDebug((new StringBuilder()).append("Scripting language : ").append(language).append(" source ").append(key != null ? (new StringBuilder()).append(" loaded with key : ").append(key).toString() : ": specified inline ").append(function == null ? "" : (new StringBuilder()).append(" function : ").append(function).toString()).toString());
        boolean returnValue;
        if(multiThreadedEngine)
            returnValue = invokeScript(synCtx);
        else
            synchronized(scriptEngine.getClass())
            {
                returnValue = invokeScript(synCtx);
            }
        if(synLog.isTraceTraceEnabled())
            synLog.traceTrace((new StringBuilder()).append("Result message after execution of script : ").append(synCtx.getEnvelope()).toString());
        if(synLog.isTraceOrDebugEnabled())
            synLog.traceOrDebug((new StringBuilder()).append("End : Script mediator return value : ").append(returnValue).toString());
        return returnValue;
    }

    private boolean invokeScript(MessageContext synCtx)
    {
        boolean returnValue;
        if(language.equals("js"))
        {
            Context cx = Context.enter();
            cx.setApplicationClassLoader(loader);
        }
        Object returnObject;
        if(key != null)
            returnObject = mediateWithExternalScript(synCtx);
        else
            returnObject = mediateForInlineScript(synCtx);
        returnValue = returnObject == null || !(returnObject instanceof Boolean) || ((Boolean)returnObject).booleanValue();
        if(language.equals("js"))
            Context.exit();
        break MISSING_BLOCK_LABEL_359;
        ScriptException e;
        e;
        handleException((new StringBuilder()).append("The script engine returned an error executing the ").append(key != null ? "external " : "inlined ").append(language).append(" script").append(key == null ? "" : (new StringBuilder()).append(" : ").append(key).toString()).append(function == null ? "" : (new StringBuilder()).append(" function ").append(function).toString()).toString(), e, synCtx);
        returnValue = false;
        if(language.equals("js"))
            Context.exit();
        break MISSING_BLOCK_LABEL_359;
        e;
        handleException((new StringBuilder()).append("The script engine returned a NoSuchMethodException executing the external ").append(language).append(" script").append(" : ").append(key).append(function == null ? "" : (new StringBuilder()).append(" function ").append(function).toString()).toString(), e, synCtx);
        returnValue = false;
        if(language.equals("js"))
            Context.exit();
        break MISSING_BLOCK_LABEL_359;
        Exception exception;
        exception;
        if(language.equals("js"))
            Context.exit();
        throw exception;
        return returnValue;
    }

    private Object mediateWithExternalScript(MessageContext synCtx)
        throws ScriptException, NoSuchMethodException
    {
        ScriptMessageContext scriptMC;
        prepareExternalScript(synCtx);
        scriptMC = new ScriptMessageContext(synCtx, xmlHelper);
        processJSONPayload(synCtx, scriptMC);
        scriptEvaluatorLock.lock();
        Object obj = invocableScript.invokeFunction(function, new Object[] {
            scriptMC
        });
        scriptEvaluatorLock.unlock();
        return obj;
        Exception exception;
        exception;
        scriptEvaluatorLock.unlock();
        throw exception;
    }

    private Object mediateForInlineScript(MessageContext synCtx)
        throws ScriptException
    {
        ScriptMessageContext scriptMC = new ScriptMessageContext(synCtx, xmlHelper);
        processJSONPayload(synCtx, scriptMC);
        Bindings bindings = scriptEngine.createBindings();
        bindings.put("mc", scriptMC);
        Object response;
        if(compiledScript != null)
            response = compiledScript.eval(bindings);
        else
            response = scriptEngine.eval(scriptSourceCode, bindings);
        return response;
    }

    private void processJSONPayload(MessageContext synCtx, ScriptMessageContext scriptMC)
        throws ScriptException
    {
        org.apache.axis2.context.MessageContext messageContext;
        String jsonString;
        Object jsonObject;
        if(!(synCtx instanceof Axis2MessageContext))
            return;
        messageContext = ((Axis2MessageContext)synCtx).getAxis2MessageContext();
        jsonString = (String)messageContext.getProperty("JSON_STRING");
        jsonObject = null;
        prepareForJSON(scriptMC);
        if(!JsonUtil.hasAJsonPayload(messageContext))
            break MISSING_BLOCK_LABEL_131;
        JsonElement o = jsonParser.parse(new JsonReader(JsonUtil.newJsonPayloadReader(messageContext)));
        if(o.isJsonNull())
        {
            logger.error("#processJSONPayload. JSON stream is not valid.");
            return;
        }
        try
        {
            jsonObject = jsEngine.eval(JsonUtil.newJavaScriptSourceReader(messageContext));
        }
        catch(Exception e)
        {
            handleException((new StringBuilder()).append("Failed to get the JSON payload from the input stream. Error>>>\n").append(e.getLocalizedMessage()).toString());
        }
        break MISSING_BLOCK_LABEL_186;
        if(jsonString != null)
        {
            String jsonPayload = jsonParser.parse(jsonString).toString();
            jsonObject = jsEngine.eval((new StringBuilder()).append('(').append(jsonPayload).append(')').toString());
        }
        if(jsonObject != null)
            scriptMC.setJsonObject(synCtx, jsonObject);
        return;
    }

    private void prepareForJSON(ScriptMessageContext scriptMC)
    {
        if(jsonParser == null)
            jsonParser = new JsonParser();
        scriptMC.setScriptEngine(jsEngine);
    }

    protected void initInlineScript()
    {
        try
        {
            initScriptEngine();
            if(scriptEngine instanceof Compilable)
            {
                if(log.isDebugEnabled())
                    log.debug("Script engine supports Compilable interface, compiling script code..");
                compiledScript = ((Compilable)scriptEngine).compile(scriptSourceCode);
            } else
            if(log.isDebugEnabled())
                log.debug("Script engine does not support the Compilable interface, in-lined script would be evaluated on each invocation..");
        }
        catch(ScriptException e)
        {
            throw new SynapseException("Exception initializing inline script", e);
        }
    }

    protected void prepareExternalScript(MessageContext synCtx)
        throws ScriptException
    {
        String generatedScriptKey;
        boolean needsReload;
        generatedScriptKey = key.evaluateValue(synCtx);
        Entry entry = synCtx.getConfiguration().getEntryDefinition(generatedScriptKey);
        needsReload = entry != null && entry.isDynamic() && (!entry.isCached() || entry.isExpired());
        resourceLock.lock();
        DataHandler dataHandler;
        BufferedReader reader;
        if(scriptSourceCode != null && !needsReload)
            break MISSING_BLOCK_LABEL_363;
        Object o = synCtx.getEntry(generatedScriptKey);
        if(o instanceof OMElement)
        {
            scriptSourceCode = ((OMElement)(OMElement)o).getText();
            scriptEngine.eval(scriptSourceCode);
            break MISSING_BLOCK_LABEL_363;
        }
        if(o instanceof String)
        {
            scriptSourceCode = (String)o;
            scriptEngine.eval(scriptSourceCode);
            break MISSING_BLOCK_LABEL_363;
        }
        if(!(o instanceof OMText))
            break MISSING_BLOCK_LABEL_363;
        dataHandler = (DataHandler)((OMText)o).getDataHandler();
        if(dataHandler == null)
            break MISSING_BLOCK_LABEL_363;
        reader = null;
        reader = new BufferedReader(new InputStreamReader(dataHandler.getInputStream()));
        StringBuilder scriptSB = new StringBuilder();
        String currentLine;
        while((currentLine = reader.readLine()) != null) 
            scriptSB.append(currentLine).append('\n');
        scriptSourceCode = scriptSB.toString();
        scriptEngine.eval(scriptSourceCode);
        IOException e;
        if(reader != null)
            try
            {
                reader.close();
            }
            // Misplaced declaration of an exception variable
            catch(IOException e)
            {
                handleException("Error in closing input stream ", e, synCtx);
            }
        break MISSING_BLOCK_LABEL_363;
        e;
        handleException("Error in reading script as a stream ", e, synCtx);
        if(reader != null)
            try
            {
                reader.close();
            }
            // Misplaced declaration of an exception variable
            catch(IOException e)
            {
                handleException("Error in closing input stream ", e, synCtx);
            }
        break MISSING_BLOCK_LABEL_363;
        Exception exception;
        exception;
        if(reader != null)
            try
            {
                reader.close();
            }
            catch(IOException e)
            {
                handleException("Error in closing input stream ", e, synCtx);
            }
        throw exception;
        resourceLock.unlock();
        break MISSING_BLOCK_LABEL_389;
        Exception exception1;
        exception1;
        resourceLock.unlock();
        throw exception1;
        Iterator i$ = includes.keySet().iterator();
_L2:
        Value includeKey;
        String includeSourceCode;
        String generatedKey;
        boolean includeEntryNeedsReload;
        if(!i$.hasNext())
            break; /* Loop/switch isn't completed */
        includeKey = (Value)i$.next();
        includeSourceCode = (String)includes.get(includeKey);
        generatedKey = includeKey.evaluateValue(synCtx);
        Entry includeEntry = synCtx.getConfiguration().getEntryDefinition(generatedKey);
        includeEntryNeedsReload = includeEntry != null && includeEntry.isDynamic() && (!includeEntry.isCached() || includeEntry.isExpired());
        resourceLock.lock();
        DataHandler dataHandler;
        BufferedReader reader;
        if(includeSourceCode != null && !includeEntryNeedsReload)
            break MISSING_BLOCK_LABEL_841;
        log.debug((new StringBuilder()).append("Re-/Loading the include script with key ").append(includeKey).toString());
        Object o = synCtx.getEntry(generatedKey);
        if(o instanceof OMElement)
        {
            includeSourceCode = ((OMElement)(OMElement)o).getText();
            scriptEngine.eval(includeSourceCode);
            break MISSING_BLOCK_LABEL_827;
        }
        if(o instanceof String)
        {
            includeSourceCode = (String)o;
            scriptEngine.eval(includeSourceCode);
            break MISSING_BLOCK_LABEL_827;
        }
        if(!(o instanceof OMText))
            break MISSING_BLOCK_LABEL_827;
        dataHandler = (DataHandler)((OMText)o).getDataHandler();
        if(dataHandler == null)
            break MISSING_BLOCK_LABEL_827;
        reader = null;
        reader = new BufferedReader(new InputStreamReader(dataHandler.getInputStream()));
        StringBuilder scriptSB = new StringBuilder();
        String currentLine;
        while((currentLine = reader.readLine()) != null) 
            scriptSB.append(currentLine).append('\n');
        includeSourceCode = scriptSB.toString();
        scriptEngine.eval(includeSourceCode);
        IOException e;
        if(reader != null)
            try
            {
                reader.close();
            }
            // Misplaced declaration of an exception variable
            catch(IOException e)
            {
                handleException("Error in closing input stream ", e, synCtx);
            }
        break MISSING_BLOCK_LABEL_827;
        e;
        handleException("Error in reading script as a stream ", e, synCtx);
        if(reader != null)
            try
            {
                reader.close();
            }
            // Misplaced declaration of an exception variable
            catch(IOException e)
            {
                handleException("Error in closing input stream ", e, synCtx);
            }
        break MISSING_BLOCK_LABEL_827;
        Exception exception2;
        exception2;
        if(reader != null)
            try
            {
                reader.close();
            }
            catch(IOException e)
            {
                handleException("Error in closing input stream ", e, synCtx);
            }
        throw exception2;
        includes.put(includeKey, includeSourceCode);
        resourceLock.unlock();
        if(true) goto _L2; else goto _L1
        Exception exception3;
        exception3;
        resourceLock.unlock();
        throw exception3;
_L1:
    }

    protected void initScriptEngine()
    {
        if(log.isDebugEnabled())
            log.debug((new StringBuilder()).append("Initializing script mediator for language : ").append(language).toString());
        ScriptEngineManager manager = new ScriptEngineManager();
        manager.registerEngineExtension("js", new RhinoScriptEngineFactory());
        manager.registerEngineExtension("groovy", new GroovyScriptEngineFactory());
        manager.registerEngineExtension("rb", new JRubyScriptEngineFactory());
        manager.registerEngineExtension("jsEngine", new RhinoScriptEngineFactory());
        manager.registerEngineExtension("py", new JythonScriptEngineFactory());
        scriptEngine = manager.getEngineByExtension(language);
        jsEngine = manager.getEngineByExtension("jsEngine");
        if(scriptEngine == null)
            handleException((new StringBuilder()).append("No script engine found for language: ").append(language).toString());
        xmlHelper = XMLHelper.getArgHelper(scriptEngine);
        multiThreadedEngine = scriptEngine.getFactory().getParameter("THREADING") != null;
        log.debug((new StringBuilder()).append("Script mediator for language : ").append(language).append(" supports multithreading? : ").append(multiThreadedEngine).toString());
    }

    public String getLanguage()
    {
        return language;
    }

    public Value getKey()
    {
        return key;
    }

    public String getFunction()
    {
        return function;
    }

    public String getScriptSrc()
    {
        return scriptSourceCode;
    }

    private void handleException(String msg)
    {
        log.error(msg);
        throw new SynapseException(msg);
    }

    public Map getIncludeMap()
    {
        return includes;
    }

    public ClassLoader getLoader()
    {
        return loader;
    }

    public void setLoader(ClassLoader loader)
    {
        this.loader = loader;
    }

    private static final Log logger = LogFactory.getLog(org/apache/synapse/mediators/bsf/ScriptMediator.getName());
    private static final String MC_VAR_NAME = "mc";
    private Value key;
    private String language;
    private final Map includes;
    private String function;
    private String scriptSourceCode;
    protected ScriptEngine scriptEngine;
    protected ScriptEngine jsEngine;
    private boolean multiThreadedEngine;
    private CompiledScript compiledScript;
    private Invocable invocableScript;
    private XMLHelper xmlHelper;
    private final Lock resourceLock;
    private Lock scriptEvaluatorLock;
    private JsonParser jsonParser;
    private ClassLoader loader;

}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\lenovo-work\localStore\eclipse\workplace\MiniSoft1.1\WebContent\WEB-INF\lib\synapse-extensions_2.1.3.wso2v11.jar
	Total time: 804 ms
	Jad reported messages/errors:
Overlapped try statements detected. Not all exception handlers will be resolved in the method mediate
Overlapped try statements detected. Not all exception handlers will be resolved in the method invokeScript
Couldn't fully decompile method invokeScript
Couldn't resolve all exception handlers in method invokeScript
Overlapped try statements detected. Not all exception handlers will be resolved in the method mediateWithExternalScript
Couldn't fully decompile method mediateWithExternalScript
Couldn't resolve all exception handlers in method mediateWithExternalScript
Couldn't resolve all exception handlers in method processJSONPayload
Overlapped try statements detected. Not all exception handlers will be resolved in the method prepareExternalScript
Couldn't fully decompile method prepareExternalScript
Couldn't resolve all exception handlers in method prepareExternalScript
	Exit status: 0
	Caught exceptions:
*/
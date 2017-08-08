/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   IndexFiles.java

package Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.pdfbox.searchengine.lucene.LucenePDFDocument;
// Referenced classes of package org.pdfbox.searchengine.lucene:
//            LucenePDFDocument

public class IndexFiles
{

    public IndexFiles()
    {
        deleting = false;
    }

    public static void main(String argv[])
    {
        String index = "index";
        boolean create = false;
        File root = null;
        String usage = "org.pdfbox.searchengine.lucene.IndexFiles [-create] [-index <index>] <root_directory>";
        if(argv.length == 0)
        {
            System.err.println("Usage: " + usage);
            return;
        }
        for(int i = 0; i < argv.length; i++)
        {
            if(argv[i].equals("-index"))
            {
                index = argv[++i];
                continue;
            }
            if(argv[i].equals("-create"))
            {
                create = true;
                continue;
            }
            if(i != argv.length - 1)
            {
                System.err.println("Usage: " + usage);
                return;
            }
            System.out.println("root=" + argv[i]);
            root = new File(argv[i]);
        }

        IndexFiles indexer = new IndexFiles();
        indexer.index(root, create, index);
    }

    public void index(File root, boolean create, String index)
    {
        try
        {
            Date start = new Date();
            writer = new IndexWriter(index, new StandardAnalyzer(), create);
            if(!create)
            {
                deleting = true;
                indexDocs(root, index, create);
            }
            indexDocs(root, index, create);
            System.out.println("Optimizing index...");
            writer.optimize();
            writer.close();
            Date end = new Date();
            System.out.print(end.getTime() - start.getTime());
            System.out.println(" total milliseconds");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void indexDocs(File file, String index, boolean create)
        throws Exception
    {
        if(!create)
        {
            reader = IndexReader.open(index);
            uidIter = reader.terms(new Term("uid", ""));
            indexDocs(file);
            if(deleting)
            {
                for(; uidIter.term() != null && uidIter.term().field().equals("uid"); uidIter.next())
                {
                    System.out.println("deleting " + HTMLDocument.uid2url(uidIter.term().text()));
                    reader.deleteDocuments(uidIter.term());
                }

                deleting = false;
            }
            uidIter.close();
            reader.close();
        } else
        {
            indexDocs(file);
        }
    }

    private void indexDocs(File file)
        throws Exception
    {
        if(file.isDirectory())
        {
            String files[] = file.list();
            Arrays.sort(files);
            for(int i = 0; i < files.length; i++)
                indexDocs(new File(file, files[i]));

        } else
        if(uidIter != null)
        {
            String uid;
            for(uid = HTMLDocument.uid(file); uidIter.term() != null && uidIter.term().field().equals("uid") && uidIter.term().text().compareTo(uid) < 0; uidIter.next())
                if(deleting)
                {
                    System.out.println("deleting " + HTMLDocument.uid2url(uidIter.term().text()));
                    reader.deleteDocuments(uidIter.term());
                }

            if(uidIter.term() != null && uidIter.term().field().equals("uid") && uidIter.term().text().compareTo(uid) == 0)
            {
                System.out.println("Next uid=" + uidIter);
                uidIter.next();
            }
        } else
        {
            try
            {
                addDocument(file);
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private void addDocument(File file)
        throws IOException, InterruptedException
    {
        String path = file.getName().toUpperCase();
        org.apache.lucene.document.Document doc = null;
        if(path.endsWith(".HTML") || path.endsWith(".HTM") || path.endsWith(".TXT"))
        {
            System.out.println("Indexing Text document: " + file);
            doc = HTMLDocument.Document(file);
        } else
        if(path.endsWith(".PDF"))
        {
            System.out.println("Indexing PDF document: " + file);
            doc = LucenePDFDocument.getDocument(file);
        } else
        {
            System.out.println("Skipping " + file);
        }
        if(doc != null)
            writer.addDocument(doc);
    }

    private boolean deleting;
    private IndexReader reader;
    private IndexWriter writer;
    private TermEnum uidIter;
}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\lenovo-work\localStore\eclipse\workplace\MiniSoft1.1\WebContent\WEB-INF\lib\compass_2.0.1.wso2v2.jar
	Total time: 225 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
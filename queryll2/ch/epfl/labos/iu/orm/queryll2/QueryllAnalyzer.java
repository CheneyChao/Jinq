package ch.epfl.labos.iu.orm.queryll2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import ch.epfl.labos.iu.orm.queryll2.path.StaticMethodAnalysisStorage;
import ch.epfl.labos.iu.orm.queryll2.path.TransformationClassAnalyzer;
import ch.epfl.labos.iu.orm.queryll2.runtime.ConfigureQueryll;

public class QueryllAnalyzer
{
   public QueryllAnalyzer()
   {
      
   }

   public void useRuntimeAnalysis(ConfigureQueryll configSource)
   {
      // Get information about how entities are configured
      ORMInformation entityInfo = new ORMInformation() {};
      configSource.configureQueryll(entityInfo);
      LambdaRuntimeTransformAnalyzer runtimeAnalyzer = new LambdaRuntimeTransformAnalyzer(entityInfo);
      QueryllSQLQueryTransformer transforms = new QueryllSQLQueryTransformer(entityInfo, runtimeAnalyzer);
      
      // Store analysis info somewhere
      configSource.storeQueryllAnalysisResults(transforms);
   }

   
   public void analyzeClassFiles(String path, ConfigureQueryll configSource) throws IOException
   {
      // Get information about how entities are configured
      ORMInformation entityInfo = new ORMInformation() {};
      configSource.configureQueryll(entityInfo);
      LambdaRuntimeTransformAnalyzer runtimeAnalyzer = new LambdaRuntimeTransformAnalyzer(entityInfo);
      QueryllSQLQueryTransformer transforms = new QueryllSQLQueryTransformer(entityInfo, runtimeAnalyzer);
      
      // Find transformation classes to analyze
      Vector<String> foundClassFiles = new Vector<String>();
      Vector<String> foundClasses = new Vector<String>();
      findClassFilesToAnalyze(path, foundClassFiles, foundClasses);
      
      // Now analyze them
      QueryllPathAnalysisSupplementalFactory pathAnalysisFactory = new QueryllPathAnalysisSupplementalFactory(entityInfo, foundClasses); 
      for (String f: foundClassFiles)
      {
         TransformationClassAnalyzer classAnalyzer = 
            new TransformationClassAnalyzer(new File(f));
         classAnalyzer.analyze(transforms, pathAnalysisFactory);
      }
      
      // Store analysis info somewhere
      configSource.storeQueryllAnalysisResults(transforms);
   }
   
   public void analyzeFiles(String[]pathsToAnalyze, String optimizationsOutputFile, String optimizationsOutputPackage) throws IOException
   {
      // Create an empty entity info for now
      ORMInformation entityInfo = new ORMInformation();
      LambdaRuntimeTransformAnalyzer runtimeAnalyzer = new LambdaRuntimeTransformAnalyzer(entityInfo);
      StaticMethodAnalysisStorage transforms = new QueryllSQLQueryTransformer(entityInfo, runtimeAnalyzer);

      // Find transformation classes to analyze
      Vector<String> foundClassFiles = new Vector<String>();
      Vector<String> foundClasses = new Vector<String>();
      for (String path: pathsToAnalyze)
         findClassFilesToAnalyze(path, foundClassFiles, foundClasses);
      
      // Now analyze them
      QueryllPathAnalysisSupplementalFactory pathAnalysisFactory = new QueryllPathAnalysisSupplementalFactory(entityInfo, foundClasses); 
      for (String f: foundClassFiles)
      {
         TransformationClassAnalyzer classAnalyzer = 
            new TransformationClassAnalyzer(new File(f));
         classAnalyzer.analyze(transforms, pathAnalysisFactory);
      }
   }
   
   // Recursively go through directories and files looking for classes
   // that are transformation classes that we should analyze and understand
   void findClassFilesToAnalyze(String path, List<String> foundClassFiles, List<String> foundClasses)
   {
      File f = new File(path);
      if (f.isFile())
      {
         if (f.getName().endsWith(".class"))
         {
            // Send each one to ASM to see if they inherit from a Queryll
            // transformation, meaning they should be analyzed more closely
            try
            {
               FileInputStream fis = new FileInputStream(f);
               String className = ASMFindQueryll2TransformClasses.isTransformation(fis); 
               if (className != null)
               {
                  foundClassFiles.add(f.getPath());
                  foundClasses.add(className);
               }
               fis.close();
            } catch (FileNotFoundException e)
            {
               e.printStackTrace();
            } catch (IOException e)
            {
               e.printStackTrace();
            }
         }
      }
      else if (f.isDirectory())
      {
         for (String name: f.list())
         {
            findClassFilesToAnalyze(new File(f, name).getPath(), foundClassFiles, foundClasses);
         }
      }
   }
}

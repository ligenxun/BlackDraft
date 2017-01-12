package yang.app.black;


import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

import yang.demo.allPurpose.fileTool;
import yang.demo.allPurpose.gitTool;
import yang.demo.allPurpose.time;
import yang.demo.allPurpose.yangIO;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JGit API����
 */
public class test {

    public static String remotePath = "https://piiiiq@bitbucket.org/piiiiq/black-project-nov.git";//Զ�̿�·��
    public static String localPath = "D:\\test\\project\\";//�������вֿ⵽����·��
    public static String initPath = "D:\\test\\testgit\\";//����·���½�
    public static String username = "piiiiq";
    public static String password = "nihaoma,.+";
    
    public static void main(String args[]) throws IOException, JGitInternalException, GitAPIException, URISyntaxException{
       
       String path = "d://test//git//test01";
        
    	//gitTool.createGitRepository(path);
//       Git git = new Git(new FileRepository(path+"/.git"));
////       RemoteAddCommand remoteAdd = git.remoteAdd();
////       remoteAdd.setUri(new URIish(remotePath));
////       remoteAdd.
//       
//       List<Ref> call = git.branchList().setListMode(ListMode.ALL).call();
//       for(Ref r:call){
//    	   System.out.println(r);
//       }
//    	System.out.println("ok");
//    	gitTool.commit(path, "ceui");
//    	gitTool.pushToRemote(path, remotePath, username, password, true);
//       LsRemoteCommand remoteCommand = Git.lsRemoteRepository();
//       Collection <Ref> refs = remoteCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
//                           .setHeads(true)
//                           .setRemote(remotePath)
//                           .call();
//
//       for (Ref ref : refs) {
//           System.out.println("Ref: " + ref.getName());
//           Git git = new Git(new FileRepository(path+"/.git"));
//           git.branchDelete().setBranchNames(ref.getName()).call();
//       }
       //gitTool.CloneFromRemote(path, remotePath, true, null, username, password);
       Git git = new Git(new FileRepository(path+"/.git"));
       List<Ref> list = git.branchList().setListMode(ListMode.REMOTE).call();
       for(Ref f:list){
    	   System.out.println(f.getName());
    	   //if(f.getName().indexOf("master") == -1)
    	   git.branchDelete().setBranchNames(f.getName()).setForce(true).call();
       }
       
       gitTool.pushToRemote(path, remotePath, username, password, true);
       
       
   
    }
    
    public void cloneAll(String host) throws InvalidRemoteException, TransportException, GitAPIException{
    	Git git = Git.cloneRepository().setURI(host).setNoCheckout(true).setCloneAllBranches(true).call();
    	List<Ref> branches = git.branchList().setListMode( ListMode.REMOTE ).call();
    	for(Ref r:branches){
    		System.out.println(r.getName());
    	}
    }
   
    //---------------
    

    /**
    * �����½��ֿ�
    */
    @Test
    public void testCreate() throws IOException {
        //�����½��ֿ��ַ
        Repository newRepo = FileRepositoryBuilder.create(new File(initPath + "/.git"));
        newRepo.create();
    }

    /**
    * ���زֿ������ļ�
    */
    @Test
    public void testAdd() throws IOException, GitAPIException {
        File myfile = new File(localPath + "/myfile.txt");
        myfile.createNewFile();
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git"));

        //����ļ�
        git.add().addFilepattern("myfile").call();
    }

    /**
    * �����ύ����
    */
    @Test
    public void testCommit() throws IOException, GitAPIException,
            JGitInternalException {
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git"));
        //�ύ����
        git.commit().setMessage("������ļ�")
        .setAuthor("������", "yangisboy@msn.com")
        //.setAll(true)
        .call();
//        git.commit().setMessage("д�����ļ�")
//        .call();
    }


    /**
    * ��ȡԶ�ֿ̲����ݵ�����
    */
    @Test
    public void testPull() throws IOException, GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
                UsernamePasswordCredentialsProvider(username,password);
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git"));
        git.pull().setRemoteBranchName("master").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    /**
    * push���ش��뵽Զ�ֿ̲��ַ
    */
    @Test
    public void testPush() throws IOException, JGitInternalException,
            GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider(username,password);
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git")); 
        
        git.push().setRemote(remotePath).setCredentialsProvider(usernamePasswordCredentialsProvider)
        .setPushAll()
        .call();
    }
}
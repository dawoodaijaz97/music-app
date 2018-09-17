package main.handlers;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import main.exceptions.NoContentException;
import main.models.User;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import java.io.InputStream;
import java.sql.SQLException;

public class BlobHandler
{
    private static String bucket = "music-app-bucket-3";
    private static final String projectID = "musicapp-03334122812";



    public static String getSong(User user,String songname) throws SQLException, ClassNotFoundException
    {
        songname = songname+"-"+user.getUserID()+".MP3";
        System.out.println("blob name = "+songname);
        System.out.println(user.getUsername());
        Storage storage =getStorage();
        System.out.println("storage = "+storage.toString());


        Bucket mybucket = getBucket(storage,bucket);

        System.out.println("bucket = "+mybucket);
        if(mybucket == null)
            throw new StorageException(0,"No Bucket Found Having Name "+bucket);

        Blob song = mybucket.get(songname);
        System.out.println("my blob = "+song);

        if(song == null)
            throw new StorageException(0,"No blob found Having Name "+songname);


        System.out.println("Media Link = "+song.getMediaLink());
        return song.getMediaLink();
    }

    public static void addSong(User user,InputStream inputStream, FormDataContentDisposition data) throws SQLException, ClassNotFoundException
    {
        String filename = data.getFileName();
        boolean flag = check_extension(filename); //checking extension of the file
        if(flag)
        {
            filename = filename.substring(0,filename.length()-4); //remove original extension with the file name
            filename = filename + "-" +Integer.toString(user.getUserID())+".MP3"; //add userID and .MP3 file extension to the file name
            System.out.println("second file name = "+filename);

            Storage storage = getStorage(); // getting storage
            if(storage == null)
                throw new StorageException(0,"No Storage Found");
            System.out.println("storage = "+storage);

            Bucket mybucket = getBucket(storage,bucket); //getting bucket
            if(mybucket == null)
                throw new StorageException(0,"No Bucket Having Name "+bucket);
            System.out.println("Bucket = "+bucket);


            Blob song = mybucket.create(filename,inputStream,"audio/mp3");
            if(song == null)
                throw new StorageException(0,"Song Not Uploaded"+filename);
            Acl acl1 = Acl.of(Acl.User.ofAllUsers(),Acl.Role.READER); //create ACL that allows all user a 'Readers' role
            song.createAcl(acl1); // add ACL to the just created blob

            System.out.println(song);

            System.out.println("song inserted in cloud storage");

        }
    }
    public static void deleteSong(User user,String songname) throws SQLException, ClassNotFoundException
    {
        songname = songname+"-"+user.getUserID()+".MP3";

        System.out.println("songname = "+songname);

        Storage storage = getStorage();
        if(storage == null)
            throw new StorageException(0,"No Storage Found");

        Bucket mybucket = getBucket(storage,bucket);

        if(mybucket == null)
            throw new StorageException(0,"No Bucket Found Having Name "+bucket);

        Blob song = mybucket.get(songname);
        System.out.println("song = "+song);
        if (song == null)
            throw new StorageException(0,"No Song Found Having Name "+songname);

        boolean flag =  song.delete();
        if(!flag)
            throw new StorageException(0,"Song Not Deleted");
    }
    private static boolean check_extension(String filename)
    {
        return filename != null && !filename.isEmpty() && filename.contains(".") && filename.endsWith(".MP3");
    }
    private static Storage getStorage()
    {
        return StorageOptions.newBuilder().setProjectId(projectID).build().getService();
    }
    private static Bucket getBucket(Storage storage,String bucketname)
    {
        Page<Bucket> bucketPage = storage.list(Storage.BucketListOption.pageSize(100),Storage.BucketListOption.prefix(""));
        Iterable<Bucket> buckets = bucketPage.getValues();
        for (Bucket bucket : buckets)
        {
            if(bucket.getName().equals(bucketname))
                return bucket;
        }
        return null;
    }
}

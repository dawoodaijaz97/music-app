package main.handlers;

import main.exceptions.InvalidRequestException;
import main.exceptions.NoContentException;
import main.exceptions.NotCreatedException;
import main.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserHandler
{
    public static List<User> getUsers() throws SQLException, ClassNotFoundException
    {
        List<User> users = new LinkedList<>();
        String query = "SELECT * FROM users";
        PreparedStatement preparedStatement  = ConnectionSingleton.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
        {
            int userID = resultSet.getInt("userID");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            users.add(new User(userID,username,password));
        }
        if(users.size() == 0)
            throw new NoContentException("No Users Found");
        return users;

    }
    public static User getUser(String username,int flag) throws SQLException, ClassNotFoundException
    {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setString(1,username);
        ResultSet resultSet =  preparedStatement.executeQuery();
        while (resultSet.next())
        {
            int userID = resultSet.getInt("userID");
            String username2 = resultSet.getString("username");
            String password = resultSet.getString("password");
            user = new User(userID,username2,password);
        }
        if(user == null && flag == 0)
            throw new NoContentException("No User found for Username "+ username );
        return  user;
    }
    public static User createUser(User user) throws SQLException, ClassNotFoundException
    {
        if(user !=null)
        {
            try
            {
                validateUserObject(user); // validates the user throws exceptions internally
                String query = "insert into users (username, password) values (?,?);";
                PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
                preparedStatement.setString(1,user.getUsername());
                preparedStatement.setString(2,user.getPassword());
                preparedStatement.execute();
                user = getUser(user.getUsername(),1);
                return user;

            }catch (SQLException e)
            {
                if(e.getErrorCode() == 1062)
                    throw new NotCreatedException("Not Created The Username Already Exists");
                else
                    throw e;
            }
        }
        else
        {
            throw new InvalidRequestException("Invalid Request No User Provided");
        }

    }
    public static int getUserID(String username) throws SQLException, ClassNotFoundException
    {
        int userID =0;
        String query = "select * from users WHERE username = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
        {
            userID = resultSet.getInt("userID");
        }
        return userID;
    }
    public static boolean checkUser(int userID) throws SQLException, ClassNotFoundException
    {
        String query = "SELECT  * FROM users WHERE userID = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,userID);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
    private static void validateUserObject(User user)
    {
        System.out.println("validating");
        boolean flag = true;

        try
        {
            if(!validateUsername(user.getUsername()))
                flag = false;
            if(!validatePassword(user.getPassword()))
                flag = false;
            if(!flag)
                throw new InvalidRequestException("Invalid Request Provide Proper User  Object {username.length <=45 & (NO SPACES),password.length <=45 & >=8 & (AT LEAST 1 UPPERCASE,1 LOWERCASE,1 NUMERIC,1 SPECIAL CHAR,NO SPACE)}");


        }catch (NullPointerException e)
        {
            throw new InvalidRequestException("Invalid Request Provide Proper Song Object {'username':'username','password':'password'");
        }
    }
    private static boolean validatePassword(String password)
    {
        boolean uppercase = false;
        boolean lowercase = false;
        boolean numeric = false;
        boolean special = false;
        boolean whitespace = false;
        char[]array = password.toCharArray();

        if(password.length() >45 || password.length() < 8)
            return false;

        for (char c : array)
        {
            if(Character.isUpperCase(c))
                uppercase = true;
            else if(Character.isLowerCase(c))
                lowercase = true;
            else if(Character.isDigit(c))
                numeric = true;
            else if (Character.isWhitespace(c))
                whitespace = true;


        }
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches())
        {
            special = true;
        }

        return uppercase && lowercase && numeric && special && !whitespace;
    }
    private static boolean validateUsername(String username)
    {


        boolean whitespace = false;
        char[] array = username.toCharArray();
        if(username.length() <5 || username.length() >45)
            return false;

        for (char c : array)
        {
            if(Character.isWhitespace(c))
                whitespace = true;
        }
        return !whitespace;
    }
}


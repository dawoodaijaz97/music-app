package main.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Error
{
    String error_msg;
    int error_code;
    int status_code;
    int response_time;
    String type;

    public Error()
    {
    }

    public Error(String error_msg, int error_code, int status_code,String type)
    {
        this.error_msg = error_msg;
        this.error_code = error_code;
        this.status_code = status_code;
        this.type = type;
    }

    public String getError_msg()
    {
        return error_msg;
    }

    public void setError_msg(String error_msg)
    {
        this.error_msg = error_msg;
    }

    public int getError_code()
    {
        return error_code;
    }

    public void setError_code(int error_code)
    {
        this.error_code = error_code;
    }

    public int getStatus_code()
    {
        return status_code;
    }

    public void setStatus_code(int status_code)
    {
        this.status_code = status_code;
    }

    public int getResponse_time()
    {
        return response_time;
    }

    public void setResponse_time(int response_time)
    {
        this.response_time = response_time;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override public String toString()
    {
        return "Error{" +
                "error_msg='" + error_msg + '\'' +
                ", error_code=" + error_code +
                ", status_code=" + status_code +
                ", response_time=" + response_time +
                ", type='" + type + '\'' +
                '}';
    }
}

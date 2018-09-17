package main.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item
{
    String item_name;

    public Item()
    {
    }

    public Item(String item_name)
    {
        this.item_name = item_name;
    }

    public String getItem_name()
    {
        return item_name;
    }

    public void setItem_name(String item_name)
    {
        this.item_name = item_name;
    }

}

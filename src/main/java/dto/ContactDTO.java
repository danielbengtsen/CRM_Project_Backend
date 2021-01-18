
package dto;

import entities.Contact;


public class ContactDTO 
{
    private String name;
    private String email;
    private String company;
    private String jobtitle;
    private String phone;

    public ContactDTO(Contact contact) 
    {
        this.name = contact.getName();
        this.email = contact.getEmail();
        this.company = contact.getCompany();
        this.jobtitle = contact.getJobtitle();
        this.phone = contact.getPhone();
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getCompany() 
    {
        return company;
    }

    public void setCompany(String company) 
    {
        this.company = company;
    }

    public String getJobtitle() 
    {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) 
    {
        this.jobtitle = jobtitle;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }
    
    
    
}

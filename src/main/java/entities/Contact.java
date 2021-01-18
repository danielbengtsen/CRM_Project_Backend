
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({
@NamedQuery(name = "Contact.getAllRows", query = "SELECT c FROM Contact c")})
@Table(name = "contact")
public class Contact implements Serializable 
{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "company", length = 255)
    private String company;
    @Column(name = "jobtitle", length = 100)
    private String jobtitle;
    @Column(name = "phone", length = 40)
    private String phone;
    
    @OneToMany(mappedBy = "contact", cascade = CascadeType.PERSIST)
    private List<Opportunity> opportunities;

    public Contact() {}

    public Contact(String name, String email, String company, String jobtitle, String phone) 
    {
        this.name = name;
        this.email = email;
        this.company = company;
        this.jobtitle = jobtitle;
        this.phone = phone;
        this.opportunities = new ArrayList();
    }
    
    public List<Opportunity> getOpportunities() 
    {
        return opportunities;
    }

    public void addOpportunity(Opportunity opportunity) 
    {
        this.opportunities.add(opportunity);
        if(opportunity != null)
        {
            opportunity.setContact(this);
        }
    }

    public Long getId() 
    {
        return id;
    }

    public void setId(Long id) 
    {
        this.id = id;
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

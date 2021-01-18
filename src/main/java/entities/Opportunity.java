
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "opportunity")
public class Opportunity implements Serializable 
{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "amount")
    private int amount;
    @Temporal(TemporalType.DATE)
    @Column(name = "close_date")
    private Date closeDate;
    
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    public Opportunity() {}

    public Opportunity(String name, int amount) 
    {
        this.name = name;
        this.amount = amount;
    }

    public Contact getContact() 
    {
        return contact;
    }

    public void setContact(Contact contact) 
    {
        this.contact = contact;
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

    public int getAmount() 
    {
        return amount;
    }

    public void setAmount(int amount) 
    {
        this.amount = amount;
    }

    public Date getCloseDate() 
    {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) 
    {
        this.closeDate = closeDate;
    }
    
    
    
}

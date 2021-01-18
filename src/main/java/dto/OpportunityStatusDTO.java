
package dto;

import entities.OpportunityStatus;

public class OpportunityStatusDTO 
{
    private String name;

    public OpportunityStatusDTO(OpportunityStatus opportunityStatus) 
    {
        this.name = opportunityStatus.getName();
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    
}

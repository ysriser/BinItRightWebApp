package tech3.binitright.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "appUadmin")
public class Admin extends BinItRightUser{

	@Column(name = "permissionUlevel")
    private String permissionLevel;

    private String department;

    @OneToMany(mappedBy = "resolvedBy", cascade = CascadeType.ALL)
    private List<Issue> issue;

    public Admin() {}
    public Admin(String permissionLevel, String department, List<Issue> issue) {
        super();
        this.permissionLevel = permissionLevel;
        this.department = department;
        this.issue = issue;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Issue> getIssue() {
        return issue;
    }

    public void setIssue(List<Issue> issue) {
        this.issue = issue;
    }


}

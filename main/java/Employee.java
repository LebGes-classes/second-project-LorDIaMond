import java.io.Serializable;

public class Employee implements Serializable {
    private String employeeID;
    private String name;
    private String position;
    private boolean isResponsible;

    public Employee(String employeeID, String name, String position, boolean isResponsible) {
        this.employeeID = employeeID;
        this.name = name;
        this.position = position;
        this.isResponsible = false;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public boolean isResponsible() {
        return isResponsible;
    }

    public void setResponsible(boolean responsible) {
        isResponsible = responsible;
    }

    public String toString() {
        return "Employee{" +
                "id='" + employeeID + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}


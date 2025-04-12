import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guest {
    private String name;
    private String email;
    private String number;
    private int attended = 0;
    private int confirmed = 0;
    private int notShownUp = 0;
    private int declined = 0;

    public Guest(String name, String email, String number) {
        this.name = name;
        this.email = email;
        this.number = number;
    }

    public void increaseAttendance(){
        attended++;
    }

    public void increaseConfirmation(){
        confirmed++;
    }

    public void increaseNotShownUp(){
        notShownUp++;
    }

    public void increaseDeclined(){
        declined++;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}

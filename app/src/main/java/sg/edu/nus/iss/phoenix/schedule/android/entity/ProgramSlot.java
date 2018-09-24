package sg.edu.nus.iss.phoenix.schedule.android.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;

public class ProgramSlot implements Serializable {

    public LocalDateTime getDateOfProgram() {
        return dateOfProgram;
    }

    public void setDateOfProgram(LocalDateTime dateOfProgram) {
        this.dateOfProgram = dateOfProgram;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public RadioProgram getRadioProgram() {
        return radioProgram;
    }

    public void setRadioProgram(RadioProgram radioProgram) {
        this.radioProgram = radioProgram;
    }

    public User getPresenter() {
        return presenter;
    }

    public void setPresenter(User presenter) {
        this.presenter = presenter;
    }

    public User getProducer() {
        return producer;
    }

    public void setProducer(User producer) {
        this.producer = producer;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    private LocalDateTime dateOfProgram;
    private LocalTime duration;
    private RadioProgram radioProgram;
    private User presenter;
    private User producer;
    private String assignedBy;

}

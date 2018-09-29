package sg.edu.nus.iss.phoenix.schedule.android.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.Duration;
import java.util.Date;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;

public class ProgramSlot implements Serializable {

    public ProgramSlot() {}

    public ProgramSlot(ZonedDateTime dateOfProgram) {
        this.dateOfProgram = dateOfProgram;
    }

    public ProgramSlot(ZonedDateTime dateOfProgram, Duration duration, RadioProgram radioProgram, User presenter, User producer, String assignedBy) {
        this.dateOfProgram = dateOfProgram;
        this.duration = duration;
        this.radioProgram = radioProgram;
        this.presenter = presenter;
        this.producer = producer;
        this.assignedBy = assignedBy;
    }

    public ZonedDateTime getDateOfProgram() {
        return dateOfProgram;
    }

    public void setDateOfProgram(ZonedDateTime dateOfProgram) {
        this.dateOfProgram = dateOfProgram;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
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

    private ZonedDateTime dateOfProgram;
    private Duration duration;
    private RadioProgram radioProgram;
    private User presenter;
    private User producer;
    private String assignedBy;


    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\nAnnualScheudle class, mapping to table annual-scheudle\n");
        out.append("Persistent attributes: \n");
        out.append("dateOfProgram = ").append(this.dateOfProgram).append("\n");
        out.append("duration = ").append(this.duration).append("\n");
        out.append("radioProgram = ").append(this.radioProgram.getRadioProgramName()).append("\n");
        out.append("presenter = ").append(this.presenter.getUserName()).append("\n");
        out.append("producer = ").append(this.producer.getUserName()).append("\n");

        return out.toString();
    }
}

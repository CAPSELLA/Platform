package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "menu_meta", schema = "food_pilot", catalog = "")
public class MenuMetaEntity {
    private int id;
    private Timestamp repeatStart;
    private int repeatInterval;
    private String repeatWeek;
    private int menuId;
    private String menuDomainName;
    private Integer week;
    private Integer day;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "repeat_start")
    public Timestamp getRepeatStart() {
        return repeatStart;
    }

    public void setRepeatStart(Timestamp repeatStart) {
        this.repeatStart = repeatStart;
    }

    @Basic
    @Column(name = "repeat_interval")
    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    @Basic
    @Column(name = "repeat_week")
    public String getRepeatWeek() {
        return repeatWeek;
    }

    public void setRepeatWeek(String repeatWeek) {
        this.repeatWeek = repeatWeek;
    }

    @Basic
    @Column(name = "menu_id")
    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Basic
    @Column(name = "menu_Domain_name")
    public String getMenuDomainName() {
        return menuDomainName;
    }

    public void setMenuDomainName(String menuDomainName) {
        this.menuDomainName = menuDomainName;
    }

    @Basic
    @Column(name = "week")
    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    @Basic
    @Column(name = "day")
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuMetaEntity that = (MenuMetaEntity) o;
        return id == that.id &&
                repeatInterval == that.repeatInterval &&
                menuId == that.menuId &&
                Objects.equals(repeatStart, that.repeatStart) &&
                Objects.equals(repeatWeek, that.repeatWeek) &&
                Objects.equals(menuDomainName, that.menuDomainName) &&
                Objects.equals(week, that.week) &&
                Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, repeatStart, repeatInterval, repeatWeek, menuId, menuDomainName, week, day);
    }
}

package agh.edu.pl.diet.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Note
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String title;

	@NotNull
	private String note;

	@NotNull
	private String calories;

	@NotNull
	private String totalFat;

	@NotNull
	private String totalCarbohydrate;

	@NotNull
	private String protein;

	@NotNull
	private String sodium;

	@NotNull
	private Long userId;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

	public String getTotalFat() {
		return totalFat;
	}

	public void setTotalFat(String totalFat) {
		this.totalFat = totalFat;
	}

	public String getTotalCarbohydrate() {
		return totalCarbohydrate;
	}

	public void setTotalCarbohydrate(String totalCarbohydrate) {
		this.totalCarbohydrate = totalCarbohydrate;
	}

	public String getProtein() {
		return protein;
	}

	public void setProtein(String protein) {
		this.protein = protein;
	}

	public String getSodium() {
		return sodium;
	}

	public void setSodium(String sodium) {
		this.sodium = sodium;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "Note{" +
				"id=" + id +
				", title='" + title + '\'' +
				", note='" + note + '\'' +
				", product='" + calories + '\'' +
				", product='" + totalFat + '\'' +
				", product='" + totalCarbohydrate + '\'' +
				", product='" + protein + '\'' +
				", product='" + sodium + '\'' +
				", userId=" + userId +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Note note1 = (Note) o;
		return Objects.equals(getId(), note1.getId()) &&
				Objects.equals(getTitle(), note1.getTitle()) &&
				Objects.equals(getNote(), note1.getNote()) &&
				Objects.equals(getCalories(), note1.getCalories()) &&
				Objects.equals(getTotalFat(), note1.getTotalFat()) &&
				Objects.equals(getTotalCarbohydrate(), note1.getTotalCarbohydrate()) &&
				Objects.equals(getProtein(), note1.getProtein()) &&
				Objects.equals(getSodium(), note1.getSodium()) &&
				Objects.equals(getUserId(), note1.getUserId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId(), getTitle(), getNote(), getCalories(), getTotalFat(), getTotalCarbohydrate(), getProtein(), getSodium(), getUserId());
	}
}

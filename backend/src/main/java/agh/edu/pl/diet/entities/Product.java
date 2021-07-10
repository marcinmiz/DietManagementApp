package agh.edu.pl.diet.entities;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private Integer calories;
    //	private String publicationDate;
    @ManyToOne(cascade= CascadeType.ALL)
    private User owner;
    @ManyToOne
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(
            name = "product_nutrients",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "nutrient_id")}
    )
    private Set<Nutrient> nutrients = new HashSet<>();

    private boolean approvalStatus = false;
    private Date creationDate = null;
    private Boolean productFavourite = false;

//	@Column(columnDefinition="text")
//	private String description;
//	private int inStockNumber;

    @Transient
    private MultipartFile productImage;


//	@OneToMany(mappedBy = "product")
//	@JsonIgnore
//	private List<BookToCartItem> bookToCartItemList;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

//	public String getPublicationDate() {
//		return publicationDate;
//	}
//
//	public void setPublicationDate(String publicationDate) {
//		this.publicationDate = publicationDate;
//	}
//
//	public String getLanguage() {
//		return language;
//	}
//
//	public void setLanguage(String language) {
//		this.language = language;
//	}

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


//	public int getInStockNumber() {
//		return inStockNumber;
//	}
//
//	public void setInStockNumber(int inStockNumber) {
//		this.inStockNumber = inStockNumber;
//	}

    public MultipartFile getProductImage() {
        return productImage;
    }

    public void setProductImage(MultipartFile productImage) {
        this.productImage = productImage;
    }

//	public List<BookToCartItem> getBookToCartItemList() {
//		return bookToCartItemList;
//	}
//
//	public void setBookToCartItemList(List<BookToCartItem> bookToCartItemList) {
//		this.bookToCartItemList = bookToCartItemList;
//	}

    public Set<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(Set<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public boolean isApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getProductFavourite() {
        return productFavourite;
    }

    public void setProductFavourite(Boolean productFavourite) {
        this.productFavourite = productFavourite;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
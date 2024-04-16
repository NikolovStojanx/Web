package mk.ukim.finki.wp.kol2021.restaurant.model;


import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    private String restaurantName;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<MenuItem> menuItems;


    public Menu() {
    }

    public Menu(String restaurantName, MenuType menuType, List<MenuItem> menuItems) {
        this.restaurantName = restaurantName;
        this.menuType = menuType;
        this.menuItems = menuItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}

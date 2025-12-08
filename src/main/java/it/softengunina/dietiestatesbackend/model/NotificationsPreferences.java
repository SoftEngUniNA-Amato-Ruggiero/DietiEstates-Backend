package it.softengunina.dietiestatesbackend.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.softengunina.dietiestatesbackend.listeners.NotificationsPreferencesListener;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "notifications_preferences")
@EntityListeners(NotificationsPreferencesListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class NotificationsPreferences {
    @Id
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private BaseUser user;

    @Getter
    @Setter
    private String city = null;

    @Getter
    @Setter
    private String subscriptionArn = null;

    @Getter
    @Setter
    private boolean notificationsForSaleEnabled = true;

    @Getter
    @Setter
    private boolean notificationsForRentEnabled = true;

    public NotificationsPreferences(BaseUser user) {
        this.user = user;
    }

    public boolean isEmailNotificationsEnabled() {
        return this.subscriptionArn != null && !this.subscriptionArn.isEmpty();
    }

    public String toFilterPolicyJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        if (this.city != null && !this.city.isEmpty()) {
            ArrayNode cities = mapper.createArrayNode();
            cities.add(this.city);
            root.set("city", cities);
        }

        ArrayNode types = mapper.createArrayNode();
        if (notificationsForSaleEnabled) {
            types.add("InsertionForSale");
        }
        if (notificationsForRentEnabled) {
            types.add("InsertionForRent");
        }
        if (!types.isEmpty()) {
            root.set("type", types);
        }

        try {
            return mapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}

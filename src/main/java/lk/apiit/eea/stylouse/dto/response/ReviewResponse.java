package lk.apiit.eea.stylouse.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lk.apiit.eea.stylouse.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private List<Review> reviews = new ArrayList<>();
    private Map<String, Integer> count = new HashMap<>();
    private boolean hasUserRated;
    private double average;
    @JsonIgnore
    private int one = 0;
    @JsonIgnore
    private int two = 0;
    @JsonIgnore
    private int three = 0;
    @JsonIgnore
    private int four = 0;
    @JsonIgnore
    private int five = 0;

    public ReviewResponse(List<Review> reviews) {
        this.reviews = reviews;
        this.average = calculateAverage();
        this.count.put("one", one);
        this.count.put("two", two);
        this.count.put("three", three);
        this.count.put("four", four);
        this.count.put("five", five);
    }

    private double calculateAverage() {
        if (reviews.size() > 0) {
            double total = 0;
            for (Review review : reviews) {
                int rate = review.getRate();
                countRates(rate);
                total += rate;

            }
            return total / reviews.size();
        }
        return 0.0;
    }

    private void countRates(int rate) {
        switch (rate) {
            case 1:
                one++;
                break;
            case 2:
                two++;
                break;
            case 3:
                three++;
                break;
            case 4:
                four++;
                break;
            case 5:
                five++;
                break;
        }
    }
}

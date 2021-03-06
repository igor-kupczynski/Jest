package io.searchbox.core.search.aggregation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import static io.searchbox.core.search.aggregation.AggregationField.*;

/**
 * @author cfstout
 */
public class SignificantTermsAggregation extends Aggregation {

    public static final String TYPE = "significant_terms";

    private Long totalCount;
    private List<SignificantTerm> significantTerms;

    public SignificantTermsAggregation(String name, JsonObject significantTermsAggregation) {
        super(name, significantTermsAggregation);
        significantTerms = new ArrayList<SignificantTerm>();
        for (JsonElement bucketv : significantTermsAggregation.get(String.valueOf(BUCKETS)).getAsJsonArray()) {
            JsonObject bucket = bucketv.getAsJsonObject();
            SignificantTerm term = new SignificantTerm(
                    bucket.get(String.valueOf(KEY)).getAsString(),
                    bucket.get(String.valueOf(DOC_COUNT)).getAsLong(),
                    bucket.get(String.valueOf(SCORE)).getAsDouble(),
                    bucket.get(String.valueOf(BG_COUNT)).getAsLong()
            );
            significantTerms.add(term);
        }
        totalCount = significantTermsAggregation.has(String.valueOf(DOC_COUNT)) ? significantTermsAggregation.get(String.valueOf(DOC_COUNT)).getAsLong() : null;
    }

    /**
     * @return total count of documents matching foreground aggregation if found, null otherwise
     */
    public Long getTotalCount() {
        return totalCount;
    }

    public List<SignificantTerm> getSignificantTerms() {
        return significantTerms;
    }

    public class SignificantTerm {
        private String key;
        private Long count;
        private Double score;
        private Long backgroundCount;

        public SignificantTerm(String key, Long count, Double score, Long backgroundCount) {
            this.key = key;
            this.count = count;
            this.score = score;
            this.backgroundCount = backgroundCount;
        }

        public String getKey() {
            return key;
        }

        public Long getCount() {
            return count;
        }

        public Double getScore() {
            return score;
        }

        public Long getBackgroundCount() {
            return backgroundCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            SignificantTerm rhs = (SignificantTerm) o;
            return new EqualsBuilder()
                    .append(getCount(), rhs.getCount())
                    .append(getBackgroundCount(), rhs.getBackgroundCount())
                    .append(getKey(), rhs.getKey())
                    .append(getScore(), rhs.getScore())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(getCount())
                    .append(getBackgroundCount())
                    .append(getKey())
                    .append(getScore())
                    .toHashCode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignificantTermsAggregation rhs = (SignificantTermsAggregation) o;
        return new EqualsBuilder()
                .append(getSignificantTerms(), rhs.getSignificantTerms())
                .append(getTotalCount(), rhs.getTotalCount())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getSignificantTerms())
                .append(getTotalCount())
                .toHashCode();
    }
}

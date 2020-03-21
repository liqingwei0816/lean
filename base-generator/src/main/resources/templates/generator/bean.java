package [(${packageName})].bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class [(${table.entityName})] {

[# th:each="field : ${table.fields}"]
        /**
         *   [(${field.comment})]
         */
        private [(${field.javaType})] [(${field.name})];
[/]
        @JsonIgnore
        private Integer pageNum;
        @JsonIgnore
        private Integer pageSize;
        @JsonIgnore
        private String orderBy;
}
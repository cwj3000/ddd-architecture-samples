package dev.huhao.samples.ddd.bff.adapters.inbound.graphql;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {
    private String userId;
    private String nickname;
}

package br.com.managerusers.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoginDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private String email;
    private String token;

}

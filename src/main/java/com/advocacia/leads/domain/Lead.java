package com.advocacia.leads.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "leads")
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nome é Obrigatório")
    @Size(max = 100, message = "Nome deve ter até 100 caracteres")
    private  String nome;

    @NotBlank(message = "Email é obrigatorio")
    @Email(message = "Email invalido")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Telefone deve seguir o formato E.164")
    private String telefone;

    @NotBlank(message = "Endereço obrigatorio")
    private String endereco;

    @NotBlank(message = "região obrigatorio")
    private String regiao;

    @Enumerated(EnumType.STRING)  // Mapeamento correto para enum
    @Column(name = "area_interesse")  // Nome da coluna no banco
    private AreaDireito areaInteresse;  // Nome do campo em Java

    private LocalDateTime dataRegistro = LocalDateTime.now();


}

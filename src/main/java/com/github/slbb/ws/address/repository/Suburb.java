package com.github.slbb.ws.address.repository;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Suburb {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suburb_seq")
    @SequenceGenerator(name="suburb_seq", sequenceName = "suburb_seq", allocationSize=1)
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private int postCode;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Calendar createTimestamp;
}

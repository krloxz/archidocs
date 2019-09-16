/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.krloxz.archidocs.test.petclinic.pet;

import java.security.acl.Owner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.krloxz.archidocs.test.petclinic.model.NamedEntity;
import com.krloxz.archidocs.test.petclinic.visit.Visit;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
// @Entity
// @Table(name = "pets")
public class Pet extends NamedEntity {

  // @Column(name = "birth_date")
  // @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  // @ManyToOne
  // @JoinColumn(name = "type_id")
  private PetType type;

  // @ManyToOne
  // @JoinColumn(name = "owner_id")
  private Owner owner;

  // @OneToMany(cascade = CascadeType.ALL, mappedBy = "petId", fetch = FetchType.EAGER)
  private Set<Visit> visits = new LinkedHashSet<>();

  public void setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public LocalDate getBirthDate() {
    return this.birthDate;
  }

  public PetType getType() {
    return this.type;
  }

  public void setType(final PetType type) {
    this.type = type;
  }

  public Owner getOwner() {
    return this.owner;
  }

  protected void setOwner(final Owner owner) {
    this.owner = owner;
  }

  protected Set<Visit> getVisitsInternal() {
    if (this.visits == null) {
      this.visits = new HashSet<>();
    }
    return this.visits;
  }

  protected void setVisitsInternal(final Set<Visit> visits) {
    this.visits = visits;
  }

  public List<Visit> getVisits() {
    List<Visit> sortedVisits = new ArrayList<>(getVisitsInternal());
    PropertyComparator.sort(sortedVisits,
      new MutableSortDefinition("date", false, false));
    return Collections.unmodifiableList(sortedVisits);
  }

  public void addVisit(final Visit visit) {
    getVisitsInternal().add(visit);
    visit.setPetId(this.getId());
  }

}
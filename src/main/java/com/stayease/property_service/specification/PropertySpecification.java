package com.stayease.property_service.specification;

import com.stayease.property_service.dto.request.PropertySearchRequest;
import com.stayease.property_service.entity.Amenity;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.entity.PropertyStatus;
import com.stayease.property_service.entity.Room;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class PropertySpecification{
    private PropertySpecification(){
    }

    public static Specification<Property> search(PropertySearchRequest request){
        //root - Property table
        //Query - Represents the SQL query being generated.
        //Criteria Builder - Creates SQL predicates such as equal(), like(), between(), greaterThan(), etc.
        //Predicate is a Condition in the sql query not a validation and this predicate is different from
        //Java Predicate (Return True or false) but JPA Predicate(Return Where condition)
        return (root, query, criteriaBuilder)->{
            List<Predicate> predicates=new ArrayList<>();
            if(request.getCity()!=null && !request.getCity().isBlank()){
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("city")),
                                request.getCity().toLowerCase()));
            }
            if(request.getState()!=null && !request.getState().isBlank()){
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("state")),
                                request.getState().toLowerCase()));
            }
            if(request.getCountry()!=null && !request.getCountry().isBlank()){
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("country")),
                                request.getCountry().toLowerCase()));
            }
            if(request.getPropertyStatus()!=null){
                predicates.add(criteriaBuilder.equal(root.get("status"),request.getPropertyStatus()));
            }
            else{
                predicates.add(criteriaBuilder.equal(root.get("status"),PropertyStatus.ACTIVE));
            }
            predicates.add(criteriaBuilder.isTrue(root.get("isActive")));
            predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
            query.distinct(true);
            Join<Property,Room> roomJoin=null;
            if (request.getMinPrice()!=null || request.getMaxPrice()!=null
                    || request.getSharingCapacity()!=null || request.getWashroomType()!=null){
                roomJoin = root.join("rooms");
            }
//            roomJoin.get("Price") -- Beacuse price column is present in room table not in property table
            if(request.getMinPrice()!=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(roomJoin.get("price"),
                                request.getMinPrice()));
            }
            if(request.getMaxPrice()!=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(roomJoin.get("price"),
                                request.getMaxPrice()));
            }
            if(request.getSharingCapacity()!=null){
                predicates.add(criteriaBuilder.equal(roomJoin.get("sharingCapacity"),
                                request.getSharingCapacity()));
            }
            if(request.getWashroomType()!=null){
                predicates.add(criteriaBuilder.equal(roomJoin.get("washroomType"),
                        request.getWashroomType()));
            }
            Join<Property,Amenity> amenityJoin=null;
            if (request.getAmenityIds()!=null && !request.getAmenityIds().isEmpty()) {
                amenityJoin=root.join("amenities");
            }
            if (request.getAmenityIds()!=null && !request.getAmenityIds().isEmpty()){
                predicates.add(amenityJoin.get("amenityId").in(request.getAmenityIds()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
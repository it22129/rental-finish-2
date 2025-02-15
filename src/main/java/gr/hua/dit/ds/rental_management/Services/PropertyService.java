package gr.hua.dit.ds.rental_management.Services;

import gr.hua.dit.ds.rental_management.Entities.Property;
import gr.hua.dit.ds.rental_management.Repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        return optionalProperty.orElseThrow(() -> new RuntimeException("Property not found"));
    }

    public void saveProperty(Property property) {
        if(property.getStatus() == null){
            property.setStatus(Property.PropertyStatus.PENDING);
        }
        propertyRepository.save(property);
    }

    public List<Property> searchProperties(String keyword){
        return propertyRepository.searchProperties(keyword);
    }
    public List<Property> getApprovedProperties(){
        return propertyRepository.findByStatus(Property.PropertyStatus.APPROVED);
    }

    public List<Property> getPendingProperties() {
        return propertyRepository.findByStatus(Property.PropertyStatus.PENDING);
    }

    public void updatePropertyStatus(Long id, Property.PropertyStatus status) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        property.setStatus(status);
        propertyRepository.save(property);
    }


    public void updateProperty(Long id, Property updatedProperty) {
        Property existingProperty = getPropertyById(id);
        existingProperty.setLocation(updatedProperty.getLocation());
        existingProperty.setOwner(updatedProperty.getOwner());
        propertyRepository.save(existingProperty);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
}

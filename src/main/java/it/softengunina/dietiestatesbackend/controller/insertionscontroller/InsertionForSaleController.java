package it.softengunina.dietiestatesbackend.controller.insertionscontroller;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionRequest;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.insertionsrepository.InsertionForSaleRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import jakarta.validation.Valid;
import org.geojson.Feature;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * Controller for handling requests related to insertions for sale.
 */
@RestController
@RequestMapping("/insertions/for-sale")
public class InsertionForSaleController {
    private final InsertionForSaleRepository insertionForSaleRepository;
    private final BusinessUserRepository businessUserRepository;
    private final InsertionDTOVisitorImpl insertionDTOVisitor;
    private final TokenService tokenService;

    public InsertionForSaleController(InsertionForSaleRepository insertionForSaleRepository,
                                      BusinessUserRepository businessUserRepository,
                                      InsertionDTOVisitorImpl insertionDTOVisitor,
                                      TokenService tokenService) {
        this.insertionForSaleRepository = insertionForSaleRepository;
        this.businessUserRepository = businessUserRepository;
        this.insertionDTOVisitor = insertionDTOVisitor;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves a paginated list of all insertions for sale.
     *
     * @param pageable Pagination information.
     * @return A page of insertion DTOs.
     */
    @GetMapping
    public Page<InsertionDTO> getInsertions(Pageable pageable) {
        return insertionForSaleRepository.findAll(pageable).map(i -> i.accept(insertionDTOVisitor));
    }

    /**
     * Creates a new insertion for sale.
     *
     * @param req The insertion data.
     * @return The created insertion DTO.
     * @throws ResponseStatusException if the user is not a business user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsertionDTO createInsertion(@Valid @RequestBody InsertionRequest req) {
        BusinessUser uploader = businessUserRepository.findByUser_CognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot create an insertion."));

        Feature feature = req.getAddress().getFeatures().getFirst();
        Map<String, Object> properties = feature.getProperties();
        String city = properties.get("city").toString();
        String province = properties.get("county").toString();
        String postalCode = properties.get("postcode").toString();
        String street = properties.get("street").toString();
        Double lat = (Double) properties.get("lat");
        Double lon = (Double) properties.get("lon");
        Point location = new org.locationtech.jts.geom.GeometryFactory().createPoint(new Coordinate(lon, lat));

        Address address = new Address(city, province, postalCode, street, location);

        InsertionForSale insertion = insertionForSaleRepository.save(new InsertionForSale(address, req.getDetails(), uploader.getUser(), uploader.getAgency(), req.getPrice()));
        return insertion.accept(insertionDTOVisitor);
    }
}

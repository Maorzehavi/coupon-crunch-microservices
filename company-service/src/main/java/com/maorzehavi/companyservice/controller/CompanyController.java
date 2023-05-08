package com.maorzehavi.companyservice.controller;

import com.maorzehavi.companyservice.model.dto.request.CompanyRequest;
import com.maorzehavi.companyservice.model.dto.response.CompanyResponse;
import com.maorzehavi.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final RestTemplate restTemplate;
    private final CompanyService companyService;


    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompany(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id " + id + " not found")));
    }


    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest companyRequest) {
       var companyResponse = companyService.createCompany(companyRequest).orElseThrow(
               () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company with name " + companyRequest.getName() + " already exists"));
         return ResponseEntity.created(URI.create("/api/v1/companies/" + companyResponse.getId()))
                    .body(companyResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody CompanyRequest companyRequest) {
        try {
            return ResponseEntity.ok(companyService.updateCompany(id, companyRequest).orElseThrow());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyResponse> deleteCompany(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(companyService.deleteCompany(id).orElseThrow());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}

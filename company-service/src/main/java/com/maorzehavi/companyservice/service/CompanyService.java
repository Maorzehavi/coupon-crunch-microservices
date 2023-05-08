package com.maorzehavi.companyservice.service;

import com.maorzehavi.companyservice.model.dto.request.CompanyRequest;
import com.maorzehavi.companyservice.model.dto.response.CompanyResponse;
import com.maorzehavi.companyservice.model.entity.Company;
import com.maorzehavi.companyservice.repository.CompanyRepository;
import com.maorzehavi.companyservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public Optional<CompanyResponse> getCompany(Long id) {
        return companyRepository.findById(id).map(this::mapToCompanyResponse);
    }

    public Optional<Company> getCompanyEntity(Long id) {
        return companyRepository.findById(id);
    }

    public Optional<CompanyResponse> createCompany(CompanyRequest companyRequest) {
        Company company = mapToCompany(companyRequest);
        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new RuntimeException("Company with email " + company.getEmail() + " already exists");
        }
        if (companyRepository.existsByName(company.getName())) {
            throw new RuntimeException("Company with name " + company.getName() + " already exists");
        }
        var userId = restTemplate.postForObject("http://identity-service/api/v1/users?clientType=COMPANY", companyRequest.getUser(), String.class);
        if (userId == null) {
            throw new RuntimeException("Failed to create user");
        }
        company.setUserId(Long.parseLong(userId));
        companyRepository.save(company);
        return Optional.of(mapToCompanyResponse(company));
    }

    public Optional<CompanyResponse> updateCompany(Long id, CompanyRequest request, HttpServletRequest httpRequest) {
        var company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company with id " + id + " not found"));
        jwtUtil.validateUser(company.getUserId(), httpRequest);
        var isEmailTaken = restTemplate.getForEntity("http://identity-service/api/v1/users/taken?userId=" + company.getUserId() + "&email=" + request.getUser().getEmail(), Boolean.class);
        if (Boolean.TRUE.equals(isEmailTaken.getBody()) || !company.getEmail().equals(request.getUser().getEmail()) && companyRepository.existsByEmail(request.getUser().getEmail())) {
            throw new RuntimeException("User with email " + request.getUser().getEmail() + " already exists");
        }
        restTemplate.put("http://identity-service/api/v1/users/" + company.getUserId(), request.getUser());
        company.setName(request.getName());
        company.setEmail(request.getUser().getEmail());
        company.setPhoneNumber(request.getPhoneNumber());
        companyRepository.save(company);
        return Optional.of(mapToCompanyResponse(company));
    }

    public Optional<CompanyResponse> deleteCompany(Long id,HttpServletRequest httpRequest) {
        var company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company with id " + id + " not found"));
        jwtUtil.validateUser(company.getUserId(), httpRequest);
        restTemplate.delete("http://identity-service/api/v1/users/" + company.getUserId());
        companyRepository.delete(company);
        return Optional.of(mapToCompanyResponse(company));
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::mapToCompanyResponse).toList();
    }

    private CompanyResponse mapToCompanyResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .isActive(company.getIsActive())
                .build();
    }

    private Company mapToCompany(CompanyRequest companyRequest) {
        return Company.builder()
                .name(companyRequest.getName())
                .email(companyRequest.getUser().getEmail())
                .phoneNumber(companyRequest.getPhoneNumber())
                .isActive(false)
                .build();
    }


}

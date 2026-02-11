package techthree.binitright.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import techthree.binitright.JwtAuthFilter;
import techthree.binitright.model.DropOffLocation;
import techthree.binitright.request.NearByBinDto;
import techthree.binitright.service.DropOffLocationImplementation;
import techthree.binitright.util.JwtUtil;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = DropOffLocationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
class DropOffLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DropOffLocationImplementation service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void getAllBins_ShouldReturnEntityList() throws Exception {

        DropOffLocation bin = new DropOffLocation();
        bin.setId("BIN-001");
        bin.setName("Green Valley Bin");
        bin.setLatitude(new BigDecimal("1.3521"));
        bin.setLongitude(new BigDecimal("103.8198"));
        bin.setStatus(DropOffLocation.Status.ACTIVE);

        when(service.getAllBins()).thenReturn(List.of(bin));


        mockMvc.perform(get("/api/bins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("BIN-001"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].latitude").value(1.3521));
    }

    @Test
    @WithMockUser
    void nearbyBins_ShouldHandleDoubleParams() throws Exception {

        NearByBinDto dto = new NearByBinDto();
        dto.setBinType("Recyclable");

        when(service.getNearbyBins(1.35, 103.82, 30.0)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/bins/nearby")
                        .param("lat", "1.35")
                        .param("lng", "103.82"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].binType").value("Recyclable"));
    }
}
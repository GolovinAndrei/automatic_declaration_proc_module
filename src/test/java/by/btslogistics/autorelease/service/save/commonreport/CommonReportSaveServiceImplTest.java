package by.btslogistics.autorelease.service.save.commonreport;

import by.btslogistics.autorelease.dao.model.commonreport.CommonReport;
import by.btslogistics.autorelease.dao.repository.commonreport.CommonReportRepository;
import by.btslogistics.autorelease.service.dto.commonreport.CommonReportDto;
import by.btslogistics.autorelease.service.mapper.commonreport.CommonReportMapper;
import by.btslogistics.autorelease.service.save.commonreport.impl.CommonReportSaveServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CommonReportSaveServiceImplTest {

    @Mock
    private CommonReportMapper commonReportMapper;

    @Mock
    private CommonReportRepository commonReportRepository;

    @InjectMocks
    private CommonReportSaveServiceImpl commonReportSaveService;

    private CommonReport commonReport;

    private CommonReportDto commonReportDto;

    String toMessagesLogId = "8gd56y836hre74g1fht7g41h1hd587d2";
    String id = "58fg52v6fr8tr7y4hgf25841hh236fdc";
    String description = "New description";
    String numberGrafa = "37";
    String codeError = "2.5";

    @BeforeAll
    public void setUp() {

        commonReportSaveService = new CommonReportSaveServiceImpl(commonReportRepository, commonReportMapper);
        commonReport = new CommonReport();
        commonReport.setId(id);
        commonReport.setToMessageLogId(toMessagesLogId);
        commonReport.setNumberGrafa(numberGrafa);
        commonReport.setCodeError(codeError);
        commonReport.setDescriptionError(description);
        commonReportDto = new CommonReportDto();
        commonReportDto.setId(id);
        commonReportDto.setToMessageLogId(toMessagesLogId);
        commonReportDto.setDescriptionError(description);
        commonReportDto.setCodeError(codeError);
        commonReportDto.setNumberGrafa(numberGrafa);
    }

    @Test
    public void saveNewReportTest() {

        Mockito.lenient().when(commonReportMapper.toEntity(commonReportDto)).thenReturn(commonReport);
        Mockito.lenient().when(commonReportMapper.toDto(commonReport)).thenReturn(commonReportDto);
        Mockito.lenient().when(commonReportRepository.save(commonReport)).thenReturn(commonReport);

        Assert.assertEquals(commonReportSaveService.addNewReport(numberGrafa, codeError, description), commonReportDto);

    }
}

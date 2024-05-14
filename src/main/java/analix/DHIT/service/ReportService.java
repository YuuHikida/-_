package analix.DHIT.service;

import analix.DHIT.exception.ReportNotFoundException;
import analix.DHIT.input.ReportSortInput;
import analix.DHIT.input.ReportUpdateInput;
import analix.DHIT.mapper.ReportMapper;
import analix.DHIT.model.Report;
import analix.DHIT.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {
    private final ReportMapper reportMapper;
    private final ReportRepository reportRepository;

    public ReportService(ReportMapper reportMapper, ReportRepository reportRepository) {
        this.reportMapper = reportMapper;
        this.reportRepository = reportRepository;
    }

    public String searchId(int employeeCode, LocalDate date) {
        return reportMapper.selectIdByEmployeeCodeAndDate(employeeCode, date);
    }

    public Report getReportById(int reportId) {
        //↓ @Select("SELECT * FROM report WHERE id = #{reportId}")これが入る
        Report report = reportRepository.findById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("Report Not Found");
        }
        return report;
    }

    public String getBeforeIdById(int reportId) {
        return reportMapper.selectBeforeIdById(reportId);
    }

    public String getAfterIdById(int reportId) {
        return reportMapper.selectAfterIdById(reportId);
    }

    public String getLatestIdByEmployeeCode(int employeeCode) {
        return reportMapper.selectLatestIdByEmployeeCode(employeeCode);
    }

    public int create(
            int employeeCode,
            String condition,
            String impressions,
            String tomorrowSchedule,
            LocalDate date,
            LocalTime endTime,
            LocalTime startTime,
            boolean isLateness,
            String latenessReason,
            boolean isLeftEarly,
            int conditionRate
    ) {

        Report newReport = new Report();
        newReport.setEmployeeCode(employeeCode);
        newReport.setCondition(condition);
        newReport.setConditionRate(conditionRate);
        newReport.setImpressions(impressions);
        newReport.setTomorrowSchedule(tomorrowSchedule);
        newReport.setDate(date);

        newReport.setEndTime(adjustTime(endTime));
        newReport.setStartTime(adjustTime(startTime));

        newReport.setIsLateness(isLateness);
        newReport.setLatenessReason(latenessReason);
        newReport.setIsLeftEarly(isLeftEarly);

        this.reportRepository.save(newReport);

        return newReport.getId();

    }

    public boolean existsReport(int employeeCode, LocalDate date) {
        int count = reportMapper.countReportByEmployeeCodeAndDate(employeeCode, date);
        return count > 0;
    }

    private LocalTime adjustTime(LocalTime time) {
        // 15分単位で切り捨てする
        LocalTime truncatedTime = time.truncatedTo(ChronoUnit.MINUTES);
        return truncatedTime.minusMinutes(truncatedTime.getMinute() % 15);
    }

    public void deleteById(int reportId) {
        this.reportRepository.deleteById(reportId);
    }

    public void update(ReportUpdateInput reportUpdateInput){

        Report report = this.reportMapper.SelectById(reportUpdateInput.getReportId());

        report.setStartTime(reportUpdateInput.getStartTime());
        report.setEndTime(reportUpdateInput.getEndTime());
        report.setIsLateness(reportUpdateInput.getIsLateness());
        report.setLatenessReason(reportUpdateInput.getLatenessReason());
        report.setIsLeftEarly(reportUpdateInput.getIsLeftEarly());
        report.setCondition(reportUpdateInput.getCondition());
        report.setTomorrowSchedule(reportUpdateInput.getTomorrowSchedule());
        report.setImpressions(reportUpdateInput.getImpressions());
        report.setConditionRate(reportUpdateInput.getConditionRate());

        this.reportRepository.update(report);

    }

    //reportテーブルのemployeeCodeに紐づいているId全取得
    public  List<Integer> getIdsByEmployeeCode(int employeeCode){
        return this.reportMapper.selectIdsByEmployeeCode(employeeCode);
    }

    public List<Report> getLastTwoByUser(int employeeCode){
        return this.reportMapper.selectLastTwoReportByEmployeeCode(employeeCode);
    }

    public Report getLastOneByUser(int employeeCode){
        return this.reportMapper.selectLastOneReportByEmployeeCode(employeeCode);
    }

    public Report getOneByUserByDate(int employeeCode, LocalDate date){
        return this.reportMapper.selectByEmployeeCodeAndDate(employeeCode, date);
    }

    public void deleteByUser(int employeeCode) {this.reportMapper.deleteByEmployeeCode(employeeCode);}

    //追記*****************************************************
    //報告一覧表示----------------------------------
    public  List<Report> getfindAll(int employeecode){
        List<Report> reports = reportRepository.findAll(employeecode);
        //日付を軸に降順に並び替える
        Collections.sort(reports, Comparator.comparing(Report::getDate).reversed());
        return  reports;
    }
    //ソート用
    public  List<Report> getSorrtReport(ReportSortInput reportSortInput) {
        List<Report> reports = reportRepository.sortReport(reportSortInput);
        Collections.sort(reports, Comparator.comparing(Report::getDate).reversed());
        return  reports;
    }
    public  Report getEmployrrCode(int reportId){
        return reportRepository.findById(reportId);
    }
}

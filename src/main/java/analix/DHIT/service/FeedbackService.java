package analix.DHIT.service;

import analix.DHIT.exception.ReportNotFoundException;
import analix.DHIT.input.FeedbackUpdateInput;
import analix.DHIT.input.ReportSortInput;
import analix.DHIT.input.ReportUpdateInput;
import analix.DHIT.mapper.FeedbackMapper;
import analix.DHIT.mapper.ReportMapper;
import analix.DHIT.model.Feedback;
import analix.DHIT.model.Report;
import analix.DHIT.repository.FeedbackRepository;
import analix.DHIT.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackMapper feedbackMapper, FeedbackRepository feedbackRepository) {
        this.feedbackMapper=feedbackMapper;
        this.feedbackRepository=feedbackRepository;
    }

    public Feedback getFeedbackById(int reportId){
        Feedback feedback = feedbackRepository.findById(reportId);
//        if(feedback == null) {
//            //今はReportだが、Feedback用も作って変える******************************
//            throw new ReportNotFoundException("Report Not Found");
//        }
        return feedback;
    }

    public int create(FeedbackUpdateInput feedbackUpdateInput) {
        Feedback newFeedback = new Feedback();
        newFeedback.setName(feedbackUpdateInput.getName());
        newFeedback.setRating(feedbackUpdateInput.getRating());
        newFeedback.setComment(feedbackUpdateInput.getComment());
        newFeedback.setReportId(feedbackUpdateInput.getReportId());

        this.feedbackRepository.save(newFeedback);

        return newFeedback.getFeedbackId();
    }

    public void deleteById(int reportId) {
        this.feedbackRepository.deleteById(reportId);
    }

    public void update(){

    }

    public boolean count(int reportId){
        int count = feedbackRepository.count(reportId);
        return count > 0;
    }
}

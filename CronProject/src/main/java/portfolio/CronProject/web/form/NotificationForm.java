package portfolio.CronProject.web.form;

import lombok.Data;

import java.util.List;

@Data
public class NotificationForm {
    private Integer status;
    private List<Long> tagList;
    private List<Long> removeList;
}

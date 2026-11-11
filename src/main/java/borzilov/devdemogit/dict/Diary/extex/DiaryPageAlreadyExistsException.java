package borzilov.devdemogit.dict.Diary.extex;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class DiaryPageAlreadyExistsException extends  RuntimeException{
    public DiaryPageAlreadyExistsException(@NotNull LocalDate date) {
        super("Diary page for date \" + date + \" already exists");
    }

}

package finalproject.soundcloud.controller;

import finalproject.soundcloud.model.pojos.ErrorMessage;
import finalproject.soundcloud.model.pojos.User;
import finalproject.soundcloud.util.exceptions.*;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;


public abstract class SessionManagerController {

    static Logger logger = Logger.getLogger(SessionManagerController.class.getName());

    public static final String LOGGED = "logged";
    @ExceptionHandler({InvalidActionException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleInvalidAction(Exception e){
        logger.error(e.getMessage());
        return new ErrorMessage(e.getMessage(),HttpStatus.UNAUTHORIZED.value(),LocalDateTime.now());
    }
    @ExceptionHandler({DoesNotExistException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleNotFound(Exception e){
        logger.error(e.getMessage());
        return new ErrorMessage(e.getMessage(),HttpStatus.BAD_REQUEST.value(),LocalDateTime.now());
    }
    @ExceptionHandler({NotLoggedException.class, UserNotFoundException.class , UnauthorizedUserException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleNotLogged(Exception e){
        logger.error(e.getMessage());
        ErrorMessage msg = new ErrorMessage(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        return msg;
    }

    @ExceptionHandler({SoundCloudException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMyErrors(Exception e){
        logger.error(e.getMessage());
        ErrorMessage msg = new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return msg;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleOtherErrors(Exception e){
        logger.error(e.getMessage());
        ErrorMessage msg = new ErrorMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        return msg;
    }

    //Session check
    public static User getLoggedUser(HttpSession session) throws SoundCloudException {
        if(session.isNew() || session.getAttribute(LOGGED) == null) {
            throw new NotLoggedException();
        }
        User user = (User) session.getAttribute(LOGGED);
        if(user.getIs_active() == 0){
            throw new InvalidUserInputException("Your profile isn't activeted");
        }
        return user;
    }

    public static void logUser(HttpSession session, User user){
        session.setAttribute(LOGGED,user);
    }

    public static void logOutUser(HttpSession session) throws Exception{
        if(getLoggedUser(session)!=null) {
            session.setAttribute(LOGGED, null);
            return;
        }
        throw new NotLoggedException();
    }

}

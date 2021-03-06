package seedu.typed;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.typed.commons.core.Config;
import seedu.typed.commons.core.EventsCenter;
import seedu.typed.commons.core.LogsCenter;
import seedu.typed.commons.core.Version;
import seedu.typed.commons.events.ui.ExitAppRequestEvent;
import seedu.typed.commons.exceptions.DataConversionException;
import seedu.typed.commons.exceptions.IllegalValueException;
import seedu.typed.commons.util.ConfigUtil;
import seedu.typed.commons.util.StringUtil;
import seedu.typed.logic.Logic;
import seedu.typed.logic.LogicManager;
import seedu.typed.model.Model;
import seedu.typed.model.ModelManager;
import seedu.typed.model.ReadOnlyTaskManager;
import seedu.typed.model.TaskManager;
import seedu.typed.model.UserPrefs;
import seedu.typed.model.util.SampleDataUtil;
import seedu.typed.storage.Storage;
import seedu.typed.storage.StorageManager;
import seedu.typed.storage.temp.Session;
import seedu.typed.ui.Ui;
import seedu.typed.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(0, 5, 0, false);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;
    protected Session session;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing Typed ]===========================");
        super.init();

        session = new Session();

        config = initConfig(getApplicationParameter("config"));

        storage = new StorageManager(config.getTaskManagerFilePath(), config.getUserPrefsFilePath());

        userPrefs = initPrefs(config);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, session, config, storage);

        ui = new UiManager(logic, config, userPrefs, model);

        initEventsCenter();
    }

    private String getApplicationParameter(String parameterName) {
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    private Model initModelManager(Storage storage, UserPrefs userPrefs) throws IllegalValueException {
        Optional<ReadOnlyTaskManager> taskManagerOptional;
        ReadOnlyTaskManager initialData;
        try {
            taskManagerOptional = storage.readTaskManager();
            if (!taskManagerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample TaskManager.");
            }
            initialData = taskManagerOptional.orElseGet(SampleDataUtil::getSampleTaskManager);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty TaskManager.");
            initialData = new TaskManager();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty TaskManager.");
            initialData = new TaskManager();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    protected Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties.");
            initializedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are
        // new or unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    protected UserPrefs initPrefs(Config config) {
        assert config != null;

        String prefsFilePath = config.getUserPrefsFilePath();
        logger.info("Using prefs file: " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException dce) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs.");
            initializedPrefs = new UserPrefs();
        } catch (IOException ioe) {
            logger.warning("Problem while reading from the file. Will be starting with an empty TaskManager.");
            initializedPrefs = new UserPrefs();
        }

        // Update prefs file in case it was missing to begin with or there are
        // new or unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException ioe) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(ioe));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Typed " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Typed ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

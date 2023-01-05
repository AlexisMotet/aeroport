/*
 * 
 */
package enstabretagne.base.logger.loggerimpl.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.Temporal;
import java.util.HashMap;
import enstabretagne.base.Settings;
import enstabretagne.base.logger.IRecordable;
import enstabretagne.base.logger.LogLevels;
import enstabretagne.base.logger.LoggerConf;
import enstabretagne.base.logger.LoggerParamsNames;
import enstabretagne.base.logger.loggerimpl.AbstractLogger;
import enstabretagne.base.logger.loggerimpl.ObjectAnalyseForLog;
import enstabretagne.base.messages.MessagesLogger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.ScenarioId;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVDataLogger.
 */
public class CSVDataLogger extends AbstractLogger {
	
	/** The default writer. */
	FileWriter defaultWriter;
	
	/** The liste fichier. */
	HashMap<String, FileWriter> listeFichier;
	
	/** The directory. */
	File directory;
	
	/** The sep. */
	final char sep = Settings.sep();

	/** The start record time. */
	LogicalDateTime startRecordTime;

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#open(enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@Override
	public boolean open(LoggerConf conf) {
		boolean success = true;

		if (conf.parametres.containsKey(LoggerParamsNames.RecordStartTime.toString())) {
			startRecordTime = LogicalDateTime
					.LogicalDateFrom(conf.parametres.get(LoggerParamsNames.RecordStartTime.toString()));
			if (startRecordTime == null)
				System.exit(1);
		}

		listeFichier = new HashMap<String, FileWriter>();
		String dirName;
		String fileName;
		if (conf.parametres.containsKey(LoggerParamsNames.DirectoryName.toString())) {
			dirName = conf.parametres.get(LoggerParamsNames.DirectoryName.toString()).toString();
			if (dirName.startsWith("."))
				dirName = System.getProperty("user.dir") + File.separator + dirName;
		} else
			dirName = System.getProperty("user.dir");
		if (conf.parametres.containsKey(LoggerParamsNames.FileName.toString())) {
			fileName = System.currentTimeMillis()
					+ conf.parametres.get(LoggerParamsNames.FileName.toString()).toString();

		} else
			fileName = "default.csv";

		directory = new File(dirName);
		directory.mkdirs();
		try {
			defaultWriter = new FileWriter(new File(dirName + "\\" + fileName));
			String header="Scenario"+sep+"Replique"+sep+"Temps Reel"+sep+"Temps Logique"+sep+"Niveau de Log"+sep+"Nom Objet"+sep+"Fonction"+sep+"Message"+"\n";
			defaultWriter.write(header);

		} catch (IOException e) {
			success = false;
			System.err.println("Logger " + this.getClass().getCanonicalName() + " n'a pu être créé.)");
			System.err.println(dirName + "\\" + fileName
					+ " est sans doute ouvert ou n'existe pas (chemin non existant au préalable par exemple)");
		}
		return success;
	}

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#log(java.lang.StackTraceElement, enstabretagne.simulation.components.ScenarioId, java.time.temporal.Temporal, enstabretagne.base.time.LogicalDateTime, enstabretagne.base.logger.LogLevels, java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void log(StackTraceElement el, ScenarioId scenarioId, Temporal t, LogicalDateTime d, LogLevels level,
			Object obj, String function, String message, Object... args) {
		if (startRecordTime != null && d.compareTo(startRecordTime) < 0)
			return;

		String toBeWritten;
		if (!level.equals(LogLevels.dataRecordable) && !level.equals(LogLevels.data) && !level.equals(LogLevels.dataSimple)) {

			if (d == null) {
				if (obj != null)
					toBeWritten = String.format(
							scenarioId.getScenarioId() + sep + scenarioId.getRepliqueNumber() + sep + t.toString() + sep + "PAS DE DATE LOGIQUE"
									+ sep + level + sep + obj.toString() + sep + function + sep + message,args);
				else
					toBeWritten = String.format(
							scenarioId.getScenarioId() + sep + scenarioId.getRepliqueNumber() + sep + t.toString() + sep + "PAS DE DATE LOGIQUE"
									+ sep + level + sep + "" + sep + function + sep + message,args);
			} else {
				if (obj != null)
					toBeWritten = String.format(
							scenarioId.getScenarioId() + sep + scenarioId.getRepliqueNumber() + sep + t.toString() + sep + d
									+ sep + level + sep + obj.toString() + sep + function + sep + message,args);
				else
					toBeWritten = String.format(
							scenarioId.getScenarioId() + sep + scenarioId.getRepliqueNumber() + sep + t.toString() + sep + d
									+ sep + level + sep + "" + sep + function + sep + message,args);
			}
			toBeWritten = toBeWritten +"\n";
			try {
				defaultWriter.write(toBeWritten);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (level.equals(LogLevels.data)) {
			if (obj == null) {
				return;
			} else {
				FileWriter fw = null;
				ObjectAnalyseForLog objAbilities = (ObjectAnalyseForLog) obj;
				
				if (listeFichier.containsKey(objAbilities.getObjectType().getName()))
					fw = listeFichier.get(objAbilities.getObjectType().getName());
				else {
					try {
						fw = new FileWriter(directory.getPath() + "\\" +System.currentTimeMillis()+ objAbilities.getClassement() + ".csv");
						listeFichier.put(objAbilities.getObjectType().getName(), fw);
						String header="Scenario"+sep+"Replique"+sep+"Temps Reel"+sep+"Temps Logique"+sep+"Nom Objet";
						for(int i=0;i<objAbilities.getTitles().length;i++)
							header=header+sep+(objAbilities.getTitles())[i];
						header=header+"\n";
						fw.write(header);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				toBeWritten = 
						scenarioId.getScenarioId() + sep + scenarioId.getRepliqueNumber() + sep + t.toString() + sep + d
								+sep + objAbilities.toString();
				for(int i=0;i<objAbilities.getRecords().length;i++)
					toBeWritten=toBeWritten+sep+(objAbilities.getRecords())[i];
				toBeWritten=toBeWritten+"\n";
				try {
					fw.write(toBeWritten);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		
		} else if(level.equals(LogLevels.dataSimple) || level.equals(LogLevels.dataRecordable)){
			if (obj == null) {
				return;
			} else {
				FileWriter fw = null;
				IRecordable objAbilities = (IRecordable) obj;
				if (listeFichier.containsKey(objAbilities.getClassement()))
					fw = listeFichier.get(objAbilities.getClassement());
				else {
					try {
						fw = new FileWriter(directory.getPath() + "\\" +System.currentTimeMillis()+ objAbilities.getClassement() + ".csv");
						listeFichier.put(objAbilities.getClassement(), fw);
						String header="Scenario"+sep+"Replique"+sep+"Temps Reel"+sep+"Temps Logique"+sep+"Nom Objet";
						for(int i=0;i<objAbilities.getTitles().length;i++)
							header=header+sep+(objAbilities.getTitles())[i];
						header=header+"\n";
						fw.write(header);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				toBeWritten = 
						scenarioId.getScenarioId() + sep + scenarioId.getRepliqueNumber() + sep + t.toString() + sep + d
								+sep + objAbilities.toString();
				for(int i=0;i<objAbilities.getRecords().length;i++)
					toBeWritten=toBeWritten+sep+(objAbilities.getRecords())[i];
				toBeWritten=toBeWritten+"\n";
				try {
					fw.write(toBeWritten);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else
		{
			System.err.println(MessagesLogger.LoggerImpossible + "CSVDataLogger");
			System.exit(1);
		}

	}

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#close()
	 */
	@Override
	public void close() {
		try {
			defaultWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (FileWriter f : listeFichier.values())
			try {
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#clear(enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@Override
	public void clear(LoggerConf conf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		try {
			defaultWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

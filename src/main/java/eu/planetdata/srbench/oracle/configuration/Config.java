/*******************************************************************************
 * Copyright 2013 Politecnico di Milano, Universidad Politécnica de Madrid
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 * Authors: Daniele Dell'Aglio, Jean-Paul Calbimonte, Marco Balduini,
 * 			Oscar Corcho, Emanuele Della Valle
 ******************************************************************************/
package eu.planetdata.srbench.oracle.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.planetdata.srbench.oracle.io.JsonConverter;
import eu.planetdata.srbench.oracle.query.ContinuousQuery;
import eu.planetdata.srbench.oracle.query.WindowDefinition;
import eu.planetdata.srbench.oracle.result.StreamProcessorOutput;
import eu.planetdata.srbench.oracle.result.StreamProcessorOutputBuilder.R2SOperator;
import eu.planetdata.srbench.oracle.s2r.ReportPolicy;

public class Config {
	private static Config _instance = null;
	private static final Logger logger = LoggerFactory.getLogger(Config.class); 
	
	private Configuration config;
	private JsonConverter converter;				

	
	private Config(){
		converter = new JsonConverter();
		try {
			config = new PropertiesConfiguration("setup.properties");
		} catch (ConfigurationException e) {
			logger.error("Error while reading the configuration file", e);
		}
	}
	
	public File getRepoDir(){
		String dir = config.getString("importer.repo.datadir");
		File ret = new File(dir);
		return ret;
	}
	
	public ContinuousQuery getQuery(String key){
		ContinuousQuery ret = new ContinuousQuery();
		String output = config.getString(key+".output");
		switch(output){
		case "rstream":
			ret.setS2ROperator(R2SOperator.Rstream);
			break;
		case "istream":
			ret.setS2ROperator(R2SOperator.Istream);
			break;
		case "dstream":
			ret.setS2ROperator(R2SOperator.Dstream);
			break;
		default:
			ret.setS2ROperator(R2SOperator.Rstream);
		}
		ret.setBooleanQuery(config.getString(key+".booleanquery"));
		ret.setWindowDefinition(new WindowDefinition(config.getLong(key+".window.size"), config.getLong(key+".window.slide")));
		ret.setFirstT0(config.getLong(key+".window.firstt0"));
		ret.setStaticData(config.getBoolean(key+".staticdata"));

		String answer = config.getString(key+".answer");
		if(answer!=null){
			InputStream is = getClass().getClassLoader().getResourceAsStream(answer);
			StreamProcessorOutput result = converter.decodeJson(is);
			ret.setAnswer(result);
		}

		return ret;
	}
	
	public String[] getQuerySet(){
		return config.getStringArray("queryset");
	}
	
	public Long getTimeUnit(){
		return config.getLong("system.timeunit");
	}
	
	public Integer getInputStreamMaxTime(){
		return config.getInt("importer.data.maxtime");
	}
	
	public Long getInputStreamInterval(){
		return config.getLong("importer.data.interval");
	}
	
	public Set<String> getInputStreamHoles(){
		List<Object> holes = config.getList("importer.data.exclude");
		Set<String> ret = new HashSet<String>(holes.size());
		for(Object hole : holes)
			ret.add(hole.toString());
		return ret;
	}

	public ReportPolicy getPolicy(){
		ReportPolicy ret = new ReportPolicy();
		ret.setWindowClose(config.getBoolean("system.policy.windowclose"));
		ret.setNonEmptyContent(config.getBoolean("system.policy.nonemptycontent"));
		ret.setContentChange(config.getBoolean("system.policy.contentchange"));
		return ret;
	}
	
	public boolean getEmtpyRelationOutput(){
		return config.getBoolean("system.output.emptyrelation");
	}
	
	public static Config getInstance(){
		if(_instance==null)
			_instance=new Config();
		return _instance;
	}
	
}

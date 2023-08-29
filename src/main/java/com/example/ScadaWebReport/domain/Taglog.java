package com.example.ScadaWebReport.domain;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Entity
@ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "org.postgresql.Driver") 
@Table(name = "\"tag_log_2023-08\"")//, schema = "logs")
@NamedNativeQuery(
		name = "Taglog.findLatestLogForEachTag",
		query = "",
		resultClass = Taglog.class	
		)


public class Taglog {
	
	
	
	  public Taglog(int taglogId, BigInteger tag_id, Float data_value, int logtime, OffsetDateTime logdate, Integer timesource,
			  Integer qualifier) {
		super();
		this.taglogId = taglogId;
		this.tag_id = tag_id;
		this.data_value = data_value;
		this.logtime = logtime;
		this.logdate = logdate;
		this.timesource = timesource;
		this.qualifier = qualifier;
	}
	  
	  
	   public Taglog() {
	        
	    }

	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "taglog_id")
	    private int taglogId;
	  
	  @Column(name = "tag_id")
	  	private BigInteger tag_id;
	  	
	  @Column(name = "data_value", columnDefinition = "INTEGER DEFAULT 0")
	  private Float data_value;
	  	
	  @Column(name = "logtime")
	  	private long logtime;
	  	
	  @Column(name = "logdate")
	  private OffsetDateTime logdate;
	    
	  @Column(name = "timesource", columnDefinition = "INTEGER DEFAULT 0")
	    private Integer timesource;
	    
	  @Column(name = "qualifier", columnDefinition = "INTEGER DEFAULT 0")
	  
	    private Integer qualifier;

		public int getTaglog_id() {
			return taglogId;
		}

		public void setTaglog_id(int taglog_id) {
			this.taglogId = taglog_id;
		}

		public BigInteger getTag_id() {
			return tag_id;
		}

		public void setTag_id(BigInteger tag_id) {
			this.tag_id = tag_id;
		}

		public Float getData_value() {
			return data_value;
		}

		public void setData_value(Float data_value) {
			this.data_value = data_value;
		}

		public long getLogtime() {
			return logtime;
		}

		public void setLogtime(long logtime) {
			this.logtime = logtime;
		}

		public OffsetDateTime getTimestamp() {
			return logdate;
		}

		public void setTimestamp(OffsetDateTime timestamp) {
			this.logdate = timestamp;
		}

		   public OffsetDateTime getLogdate() {
		        return logdate;
		    }
		
		   public String getFormattedLogdate() {
			    if (logdate != null) {
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			        return logdate.format(formatter);
			    }
			    return "";
			}
		   
		   
		   
			public Integer getTimesource() {
				return timesource;
			}

		public void setTimesource(Integer timesource) {
			this.timesource = timesource;
		}
		
	

		public Integer getQualifier() {
			return qualifier;
		}

		public void setQualifier(Integer qualifier) {
			this.qualifier = qualifier;
		} 
	  	
	

}

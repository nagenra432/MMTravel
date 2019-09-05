package com.kuliza.mmtraverl.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GoogleDistanceMatrix {

	private List<String> destination_addresses;
	private List<String> origin_addresses;
	private List<Rows> rows;
	public class Rows{
		private List<Elements> elements;
		public class Elements{
			private InnerInfo distance;
			private InnerInfo duration;
			@NotNull
			private String status;
			public class InnerInfo{
				private String text;
				private Integer value;
				public String getText() {
					return text;
				}
				public void setText(String text) {
					this.text = text;
				}
				public Integer getValue() {
					return value;
				}
				public void setValue(Integer value) {
					this.value = value;
				}
				@Override
				public String toString() {
					return "InnerInfo [text=" + text + ", value=" + value + "]";
				}
				
			}
			public InnerInfo getDistance() {
				return distance;
			}
			public void setDistance(InnerInfo distance) {
				this.distance = distance;
			}
			public InnerInfo getDuration() {
				return duration;
			}
			public void setDuration(InnerInfo duration) {
				this.duration = duration;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			@Override
			public String toString() {
				return "Elements [distance=" + distance + ", duration=" + duration + ", status=" + status + "]";
			}
			
		}
		
		public List<Elements> getElements() {
			return elements;
		}
		public void setElements(List<Elements> elements) {
			this.elements = elements;
		}
		
		@Override
		public String toString() {
			return "Rows [elements=" + elements + "]";
		}
		
		
		
	}
	private String status;
	
}

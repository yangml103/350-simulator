package src;

public class Simulator {

	String queueType;
	Double lambdaX;
	double avgServT;
	double lambdaY;
	double avgServTY;
	double xs0tos1;
	double ys0tos1;
	double avgservtimes1;
	double avgservtimes2;
	int queuesizelimits2;
	double ps0froms1;
	double ps0froms2;
	
	
	public Simulator(double lambdaX, double avgServT, String queueType, double lambdaY, double avgservTY, double xs0tos1, double ys0tos1, double avgservtimes1, double avgservtimes2, int queuesizelimits2, double ps0froms1, double ps0froms2) {
		this.lambdaX = lambdaX;
		this.avgServT = avgServT;
		this.queueType = queueType;
		this.lambdaY = lambdaY;
		this.avgServTY = avgservTY;
		this.xs0tos1 = xs0tos1;
		this.ys0tos1 = ys0tos1;
		this.avgservtimes1 = avgservtimes1;
		this.avgservtimes2 = avgservtimes2;
		this.queuesizelimits2 = queuesizelimits2;
		this.ps0froms1 = ps0froms1;
		this.ps0froms2 = ps0froms2;

	}
	
	
	void simulate(Double time) {
		Timeline timeline = new Timeline();
		Server S0 = new Server(queueType);

		LoadGenerator loadgenerator = new LoadGenerator("ARRX",lambdaX,"X", S0);
		LoadGenerator loadgeneratorY = new LoadGenerator("ARRY", lambdaY,"Y", S0);
		
		double timestart = 0.0;
		while(timestart <= (time)) {
			Event x = loadgenerator.generateEvent(timestart);
			if(x.timestamp <= time) {
				timeline.addToTimeline(x);
			}
			timestart = x.timestamp;
		}
		
		timestart = 0.0;
		while(timestart <= (time)) {
			Event y = loadgeneratorY.generateEvent(timestart);
			if(y.timestamp <= time) {
				timeline.addToTimeline(y);
			}
			timestart = y.timestamp;
		}
		
		double averageQueueLengthNumerator = 0.0;
		double averageResponseTime = 0.0;
		int numberOfProcessedRequests = 0;
		double totalTimeInQueue = 0.0;
		double totalRequestsWaitingToBeProcessed = 0.0;
		
		double currentTime = 0.0;
		double lastProcess = 0.0;
		
		double utilizationTime = 0.0;
		
		double averageResponseTimeY = 0.0;
		double averageWaitTimeY = 0.0;

		double averageWaitTime = 0.0;

		double runs = 0.0;
		int numberOfProcessedRequestsY = 0;
		double averageTimeInQueueY = 0.0;
		double totalTimeInQueueY = 0.0;


		double averageQueueLengthNumeratorS1 = 0.0;
		double averageQueueLengthNumeratorS2 = 0.0;
		int S2Dropped = 0;
		double totalRequestsWaitingToBeProcessed1 = 0.0;
		double totalRequestsWaitingToBeProcessed2 = 0.0;
		double utilizationTime1 = 0.0;
		double utilizationTime2 = 0.0;


		//&& lastProcess < time
		Server S1 = new Server("FIFO");
		Server S2 = new Server("FIFO",queuesizelimits2);

		while(!timeline.isEmpty()) {
			Event curr = timeline.popNext();
			if (curr.timestamp >= time) {
				break;
			}
			
			double timeDelta = curr.timestamp - currentTime;
			currentTime = curr.timestamp;
			averageQueueLengthNumerator += timeDelta*S0.getQueueLength();
			averageQueueLengthNumeratorS1 += timeDelta*S1.getQueueLength();
			averageQueueLengthNumeratorS2 += timeDelta*S2.getQueueLength();

			totalRequestsWaitingToBeProcessed += timeDelta * Math.max((S0.getQueueLength() -1),0);
			totalRequestsWaitingToBeProcessed1 += timeDelta * Math.max((S1.getQueueLength() -1),0);
			totalRequestsWaitingToBeProcessed2 += timeDelta * Math.max((S2.getQueueLength() -1),0);
			//System.out.println("S2 queue length: "+ S2.getQueueLength());


			if (S0.getQueueLength() != 0) {
				utilizationTime += timeDelta;
			}
			if (S1.getQueueLength() != 0) {
				utilizationTime1 += timeDelta;
			}
			if (S2.getQueueLength() != 0) {
				utilizationTime2 += timeDelta;
			}
			
			if (curr.type == "ARRX" && curr.serverType == S0) {
				numberOfProcessedRequests ++;

				Exp len = new Exp();
				double len1 = len.getExp(1/avgServT);
				//double len1 = len.getPoisson(1/avgServT);

				System.out.println(curr.name + " " + "ARR" + ": " + curr.timestamp + " " + "LEN:" + " " + len1);
				Request request = new Request(curr.name, curr.timestamp, len1);
		
				S0.receiveRequest(request, timeline,curr.timestamp);
			}
			else if (curr.type == "ARRY"&& curr.serverType == S0) {
				numberOfProcessedRequestsY ++;

				Exp len = new Exp();
				double len1 = len.getExp(1/avgServTY);
				//double len1 = len.getPoisson(1/avgServT);

				System.out.println(curr.name + " " + "ARR" + ": " + curr.timestamp + " " + "LEN:" + " " + len1);
				Request request = new Request(curr.name, curr.timestamp, len1);
		
				S0.receiveRequest(request, timeline,curr.timestamp);
			}
			else if ((curr.type == "ARRX" || curr.type == "ARRY") && curr.serverType == S1) {
				Exp len = new Exp();
				double len1 = len.getExp(1/avgservtimes1);
				//double len1 = len.getPoisson(1/avgServT);
				

				System.out.println(curr.name + " " + "ARR" + ": " + curr.timestamp + " " + "LEN:" + " " + len1);
				Request request = new Request(curr.name, curr.timestamp, len1);
		
				S1.receiveRequest(request, timeline,curr.timestamp);
			}
			
			else if (curr.type == "ARRX"&& curr.serverType == S2) {
				Exp len = new Exp();
				double len1 = len.getExp(1/avgServTY);
				//double len1 = len.getPoisson(1/avgServT);

				System.out.println(curr.name + " " + "ARR" + ": " + curr.timestamp + " " + "LEN:" + " " + len1);
				Request request = new Request(curr.name, curr.timestamp, len1);
		
				S2.receiveRequest(request, timeline,curr.timestamp);
			}
			else if (curr.type == "ARRY"&& curr.serverType == S2) {
				Exp len = new Exp();
				double len1 = len.getExp(1/avgServTY);
				//double len1 = len.getPoisson(1/avgServT);

				System.out.println(curr.name + " " + "ARR" + ": " + curr.timestamp + " " + "LEN:" + " " + len1);
				Request request = new Request(curr.name, curr.timestamp, len1);
		
				S2.receiveRequest(request, timeline,curr.timestamp);
			}
			
			
			
			else if (curr.type == "START") {
				
				curr.serverType.currR = curr.request;
				
				double finishTime = curr.timestamp + curr.request.processTime; 
				System.out.println(curr.name + " " + curr.type + ": " + curr.timestamp);
				Event finish = new Event("DONE", finishTime, curr.name, curr.request, curr.serverType);
				
				if (curr.request.isX()) {
					totalTimeInQueue +=  (curr.timestamp - curr.request.arrivalTime);

				}
				else {
					totalTimeInQueueY += (curr.timestamp - curr.request.arrivalTime);
				}
				timeline.addToTimeline(finish);
			}
			else {
				if(curr.serverType == S0) {


					Request completedRequest = S0.removeRequest(curr.timestamp, timeline);
					//Event completedEvent = new Event("START", , curr.name, curr.request, curr.serverType);

					double random = Math.random();

					if (completedRequest.isX()) {

						if (random <= xs0tos1) {
							System.out.println(curr.name + " " + "FROM S0 TO S1" + ": " + curr.timestamp);
							Exp newTime = new Exp();
							double newTime1 = newTime.getExp(1 / avgservtimes1);
							completedRequest.processTime = newTime1;
							completedRequest.totalProcessTime += newTime1;

							averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
							//numberOfProcessedRequests ++;

							S1.receiveRequest(completedRequest, timeline, curr.timestamp);
							runs++;
							completedRequest.timeEnteredQueue = curr.timestamp;

							//totalTimeInQueue +=  averageResponseTime - newTime1
						} else {
							System.out.println(curr.name + " " + "FROM S0 TO S2" + ": " + curr.timestamp);
							Exp newTime = new Exp();
							double newTime1 = newTime.getExp(1 / avgservtimes2);
							completedRequest.processTime = newTime1;
							completedRequest.totalProcessTime += newTime1;

							averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
							//numberOfProcessedRequests ++;

							if (!S2.receiveRequest(completedRequest, timeline, curr.timestamp)) {
								System.out.println(curr.name + " " + "DROP S2");
								S2Dropped ++;

							}
							runs++;
							completedRequest.timeEnteredQueue = curr.timestamp;

						}


					} else {
						if (random <= ys0tos1) {
							System.out.println(curr.name + " " + "FROM S0 TO S1" + ": " + curr.timestamp);
							Exp newTime = new Exp();
							double newTime1 = newTime.getExp(1 / avgservtimes1);
							completedRequest.processTime = newTime1;
							completedRequest.totalProcessTime += newTime1;
							S1.receiveRequest(completedRequest, timeline, curr.timestamp);
							runs++;

							averageResponseTimeY += (curr.timestamp - completedRequest.arrivalTime);
							//numberOfProcessedRequestsY ++;

							completedRequest.timeEnteredQueue = curr.timestamp;

							//totalTimeInQueueY +=  (curr.timestamp - completedRequest.timeEnteredQueue);
						} else {
							System.out.println(curr.name + " " + "FROM S0 TO S2" + ": " + curr.timestamp);
							Exp newTime = new Exp();
							double newTime1 = newTime.getExp(1 / avgservtimes2);
							completedRequest.processTime = newTime1;
							completedRequest.totalProcessTime += newTime1;

							averageResponseTimeY += (curr.timestamp - completedRequest.arrivalTime);
							//numberOfProcessedRequestsY ++;

							if (!S2.receiveRequest(completedRequest, timeline, curr.timestamp)) {
								System.out.println(curr.name + " " + "DROP S2");
								S2Dropped ++;
							}
							runs++;
							completedRequest.timeEnteredQueue = curr.timestamp;
						}

					}
				}
					if(curr.serverType == S1) {

						
						Request completedRequest = S1.removeRequest(curr.timestamp,  timeline);
		
						double random = Math.random();
						
						if (random <= ps0froms1) {
							
							if (completedRequest.isX()) {
								
								System.out.println(curr.name + " " + "FROM S1 TO S0" + ": " + curr.timestamp);
								Exp newTime = new Exp();
								double newTime1 = newTime.getExp(1/avgServT);
								completedRequest.processTime = newTime1;
								completedRequest.totalProcessTime += newTime1;
								S0.receiveRequest(completedRequest, timeline,curr.timestamp);

								averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
								numberOfProcessedRequests ++;

								runs ++;
								completedRequest.timeEnteredQueue = curr.timestamp;
								//totalTimeInQueue +=  (curr.timestamp - completedRequest.timeEnteredQueue);
								
							}
							else {
								System.out.println(curr.name + " " + "FROM S1 TO S0" + ": " + curr.timestamp);
								Exp newTime = new Exp();
								double newTime1 = newTime.getExp(1/avgServTY);
								completedRequest.processTime = newTime1;
								completedRequest.totalProcessTime += newTime1;
								S0.receiveRequest(completedRequest, timeline,curr.timestamp);
								runs ++;

								averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
								numberOfProcessedRequests ++;

								averageResponseTimeY += (curr.timestamp - completedRequest.arrivalTime);
								numberOfProcessedRequestsY ++;

								completedRequest.timeEnteredQueue = curr.timestamp;

								//totalTimeInQueueY +=  (curr.timestamp - completedRequest.timeEnteredQueue);

							}
						}
						else {
							if(completedRequest.isX()){
								averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
								numberOfProcessedRequests ++;
								System.out.println(curr.name + " " + "FROM S1 TO OUT" + ": " + curr.timestamp);

							}
							else{
								averageResponseTimeY += (curr.timestamp - completedRequest.arrivalTime);
								numberOfProcessedRequestsY ++;
								System.out.println(curr.name + " " + "FROM S1 TO OUT" + ": " + curr.timestamp);
							}
						}
					
					}
					
						if(curr.serverType == S2) {

							
							Request completedRequest = S2.removeRequest(curr.timestamp,  timeline);
			
							double random = Math.random();
							
							if (random <= ps0froms2) {
								
								if (completedRequest.isX()) {
									
									System.out.println(curr.name + " " + "FROM S2 TO S0" + ": " + curr.timestamp);
									Exp newTime = new Exp();
									double newTime1 = newTime.getExp(1/avgServT);
									completedRequest.processTime = newTime1;
									completedRequest.totalProcessTime += newTime1;
									S0.receiveRequest(completedRequest, timeline,curr.timestamp);
									runs ++;

									averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
									numberOfProcessedRequests ++;

									completedRequest.timeEnteredQueue = curr.timestamp;
									//totalTimeInQueue +=  (curr.timestamp - completedRequest.timeEnteredQueue);
									
			
								}
								else {
									System.out.println(curr.name + " " + "FROM S2 TO S0" + ": " + curr.timestamp);
									Exp newTime = new Exp();
									double newTime1 = newTime.getExp(1/avgServTY);
									completedRequest.processTime = newTime1;
									completedRequest.totalProcessTime += newTime1;
									S0.receiveRequest(completedRequest, timeline,curr.timestamp);
									runs ++;

									averageResponseTimeY += (curr.timestamp - completedRequest.arrivalTime);
									numberOfProcessedRequestsY ++;

									completedRequest.timeEnteredQueue = curr.timestamp;
			
			
									//totalTimeInQueueY += (curr.timestamp - completedRequest.timeEnteredQueue);
											
											//(curr.timestamp - completedRequest.arrivalTime) - completedRequest.totalProcessTime;
			
									
								}
							}
							else {
								if(completedRequest.isX()){
									averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
									numberOfProcessedRequests ++;
									System.out.println(curr.name + " " + "FROM S2 TO OUT" + ": " + curr.timestamp);

								}
								else{
									averageResponseTimeY += (curr.timestamp - completedRequest.arrivalTime);
									numberOfProcessedRequestsY ++;
									System.out.println(curr.name + " " + "FROM S2 TO OUT" + ": " + curr.timestamp);
								}

							}
					
						}
				
				
				
				/*if (completedRequest.isX()) {
					averageResponseTime += (curr.timestamp - completedRequest.arrivalTime);
					numberOfProcessedRequests ++;
					totalTimeInQueue += (curr.timestamp - completedRequest.arrivalTime) - completedRequest.totalProcessTime;



				}
				else {
					averageResponseTimeY += (curr.timestamp - completedRequest.arrivalTime);
					numberOfProcessedRequestsY ++;
					totalTimeInQueueY += (curr.timestamp - completedRequest.arrivalTime) - completedRequest.totalProcessTime;

				}*/
				System.out.println(curr.name + " " + curr.type + ": " + curr.timestamp);
			}

			lastProcess = curr.timestamp;

			
		}
		double timeDelta = time - currentTime;
		
		averageQueueLengthNumerator += (time - currentTime)*S0.getQueueLength();
		double averageQueueLength = averageQueueLengthNumerator/time;

		averageQueueLengthNumeratorS1 += (time - currentTime)*S1.getQueueLength();
		double averageQueueLengthS1 = averageQueueLengthNumeratorS1/time;

		averageQueueLengthNumeratorS2 += (time - currentTime)*S2.getQueueLength();
		double averageQueueLengthS2 = averageQueueLengthNumeratorS2/time;


		averageResponseTime /= numberOfProcessedRequests;
		double averageTimeInQueue = totalTimeInQueue / numberOfProcessedRequests;
		averageTimeInQueueY = totalTimeInQueueY / numberOfProcessedRequests;

		//double utilization = (lambdaX) * avgServT;
		totalRequestsWaitingToBeProcessed += timeDelta * Math.max(S0.getQueueLength() -1, 0);
		totalRequestsWaitingToBeProcessed1 += timeDelta * Math.max(S1.getQueueLength() -1, 0);
		totalRequestsWaitingToBeProcessed2 += timeDelta * Math.max(S2.getQueueLength() -1, 0);

		double utilization = utilizationTime / time;
		double utilization1 = utilizationTime1 / time;
		double utilization2 = utilizationTime2 / time;

		double averageNumberOfRequestsWaitingForService = totalRequestsWaitingToBeProcessed/time;
		double averageNumberOfRequestsWaitingForService1 = totalRequestsWaitingToBeProcessed1/time;
		double averageNumberOfRequestsWaitingForService2 = totalRequestsWaitingToBeProcessed2/time;


		System.out.println();
		System.out.println("S0 UTIL: "+  utilization);
		System.out.println("S0 QAVG: "+  averageQueueLength);
		System.out.println("S0 WAVG: "+  averageNumberOfRequestsWaitingForService);
		
		System.out.println();
		System.out.println("S1 UTIL: "+  utilization1);
		System.out.println("S1 QAVG: "+  averageQueueLengthS1);
		System.out.println("S1 WAVG: "+  averageNumberOfRequestsWaitingForService1);
		
		System.out.println();
		System.out.println("S2 UTIL: "+  utilization2);
		System.out.println("S2 QAVG: "+  averageQueueLengthS2);
		System.out.println("S2 WAVG: "+  averageNumberOfRequestsWaitingForService2);
		System.out.println("S2 DROPPED: "+  S2Dropped);
		
		averageResponseTimeY /= numberOfProcessedRequestsY;
		averageTimeInQueueY = totalTimeInQueueY/numberOfProcessedRequestsY;
		runs /= (time*lambdaX + time*lambdaY);
		runs ++;
		//NEED TO IMPLEMENT QAVG X and Y
		System.out.println("QAVG: "+  (averageQueueLength + averageQueueLengthS1 + averageQueueLengthS2));
		//System.out.println("QAVG Y: "+  (lambdaY*averageResponseTimeY));

		System.out.println("TRESP X: "+ averageResponseTime);
		System.out.println("TRESP Y: "+ averageResponseTimeY);
		System.out.println("TWAIT X: "+ averageTimeInQueue);
		System.out.println("TWAIT Y: "+ averageTimeInQueueY);

	}
	
	public static void main(String[] args) {
		Double simTime = Double.parseDouble(args[0]);
		Double arrRateX = Double.parseDouble(args[1]);
		Double arrRateY = Double.parseDouble(args[2]);
		Double avgServT = Double.parseDouble(args[3]);
		Double avgServTY = Double.parseDouble(args[4]);
		String queueType = args[5];
		Double pXS1 = Double.parseDouble(args[6]);
		Double pYS1 = Double.parseDouble(args[7]);
		Double servTS1 = Double.parseDouble(args[8]);
		Double servTS2 = Double.parseDouble(args[9]);
		int queueSizeLimit = Integer.parseInt(args[10]);
		Double pbacktoS0fromS1 = Double.parseDouble(args[11]);
		Double pbacktoS0fromS2 = Double.parseDouble(args[12]);
		//Double pOut = Double.parseDouble(args[6]);
		Simulator sim = new Simulator(arrRateX, avgServT, queueType, arrRateY,avgServTY, pXS1, pYS1, servTS1, servTS2, queueSizeLimit, pbacktoS0fromS1, pbacktoS0fromS2);
		sim.simulate(simTime);
	}

}

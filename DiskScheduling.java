//********************************************************************
//
//  Author:        Marshal Pfluger
//
//  Program #:     Program Nine
//
//  File Name:     DiskScheduling.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Due Date:      12/04/2023
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  17
//
//  Description:    Class implements the disk scheduling algorithms  
//
//********************************************************************
import java.util.*;



class DiskScheduling {
	private int numCylinders = 5000;
    private ArrayList<Integer> cylinderRequests;
    private int numRequests;
    private int initialPosition;

    public DiskScheduling(ArrayList<Integer> cylinderRequests, int initialPosition, int numRequests) {
        this.cylinderRequests = cylinderRequests;
        this.initialPosition = initialPosition;
        this.numRequests = numRequests;
    }

    //**************************************************************
    //
    //  Method:       runFIFO
    //
    //  Description:  First-Come-First-Serve (FCFS) Disk Scheduling Algorithm
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runFCFS() {
        // Initialize variables
        int totalHeadMovement = 0;
        int currentHeadPosition = this.initialPosition;

        // Iterate through the cylinder requests
        for (int i = 0; i < this.numRequests; i++) {
            // Calculate the absolute head movement for each request
            totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            // Update the current head position
            currentHeadPosition = this.cylinderRequests.get(i);
        }

        // Return the total head movement
        return totalHeadMovement;
    }

    //**************************************************************
    //
    //  Method:       runSSTF
    //
    //  Description:  Shortest Seek Time First (SSTF) Disk Scheduling Algorithm
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runSSTF() {
        // Initialize variables
        int totalHeadMovement = 0;
        // Create a copy of the original request list
        ArrayList<Integer> requestsCopy = new ArrayList<>(this.cylinderRequests);
        int currentHeadPosition = this.initialPosition;

        // Process requests until the copy is empty
        while (!requestsCopy.isEmpty()) {
            // Find the closest request to the current head position
            int closestRequest = findClosestRequest(requestsCopy, currentHeadPosition);
            // Calculate the absolute head movement for the closest request
            totalHeadMovement += Math.abs(currentHeadPosition - closestRequest);
            // Update the current head position
            currentHeadPosition = closestRequest;
            // Remove the processed request from the copy
            requestsCopy.remove(Integer.valueOf(closestRequest));
        }

        // Return the total head movement
        return totalHeadMovement;
    }

    //**************************************************************
    //
    //  Method:       findClosestRequest
    //
    //  Description:  Helper method to find the closest request to the current head position
    //
    //  Parameters:   List<Integer> requestQueue, int currentHeadPosition
    //
    //  Returns:      int
    //
    //**************************************************************
    public int findClosestRequest(List<Integer> requestQueue, int currentHeadPosition) {
        // Sort the request queue to simplify finding the closest request
        Collections.sort(requestQueue);

        // Initialize variables for tracking the closest request and its distance
        int closestRequest = -1;
        int minDistance = 5000;

        // Iterate through the sorted request queue
        for (int request : requestQueue) {
            // Calculate the distance from the current head position to the request
            int distance = Math.abs(currentHeadPosition - request);
            // Update the closest request if the distance is smaller
            if (distance < minDistance) {
                minDistance = distance;
                closestRequest = request;
            }
        }

        // Return the closest request
        return closestRequest;
    }


    //**************************************************************
    //
    //  Method:       runScan
    //
    //  Description:  Scan Disk Scheduling Algorithm
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runScan() {
    	
        // Add the initial head position to the list
        this.cylinderRequests.add(this.initialPosition);
        this.cylinderRequests.add(0);

        // Sort the requests to represent the order of disk tracks
        Collections.sort(this.cylinderRequests);

        int totalHeadMovement = 0;
        int currentHeadPosition = this.initialPosition;

        // Find index of initial head position in the sorted list
        int initialHeadIndex = this.cylinderRequests.indexOf(this.initialPosition);

        // Move the head towards the beginning of the disk
        for (int i = initialHeadIndex - 1; i >= 0; i--) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }

        // Move the head towards the end of the disk
        for (int i = initialHeadIndex + 1; i < this.cylinderRequests.size(); i++) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }
        // Return the total head movement
        return totalHeadMovement;
    }

    //**************************************************************
    //
    //  Method:       runCScan
    //
    //  Description:  CScan Disk Scheduling Algorithm
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runCScan() {
    	this.cylinderRequests.add(this.numCylinders - 1);
        int totalHeadMovement = 0;
        int currentHeadPosition = this.initialPosition;

        // Find index of initial head position in the sorted list
        int initialHeadIndex = this.cylinderRequests.indexOf(this.initialPosition);

        // Move the head towards the beginning of the disk
        for (int i = initialHeadIndex - 1; i >= 0; i--) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }

        // Move the head towards the end of the disk
        for (int i = cylinderRequests.size() - 1; i > initialHeadIndex; i--) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }
        // Return the total head movement
        return totalHeadMovement;
    }

    //**************************************************************
    //
    //  Method:       runLook
    //
    //  Description:  Look Disk Scheduling Algorithm
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runLook() {
    	this.cylinderRequests.remove(this.cylinderRequests.indexOf(0));
    	this.cylinderRequests.remove(this.cylinderRequests.indexOf(this.numCylinders - 1));
    	
        int totalHeadMovement = 0;
        int currentHeadPosition = this.initialPosition;

        // Find index of initial head position in the sorted list
        int initialHeadIndex = this.cylinderRequests.indexOf(this.initialPosition);

        // Move the head towards the beginning of the disk
        for (int i = initialHeadIndex - 1; i >= 0; i--) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }

        // Move the head towards the end of the disk
        for (int i = initialHeadIndex + 1; i < this.cylinderRequests.size(); i++) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }
        // Return the total head movement
        return totalHeadMovement;
    }

    //**************************************************************
    //
    //  Method:       runCLook
    //
    //  Description:  CLook Disk Scheduling Algorithm
    //
    //  Parameters:   N/A
    //
    //  Returns:      int
    //
    //**************************************************************
    public int runCLook() {
        int totalHeadMovement = 0;
        int currentHeadPosition = this.initialPosition;

        // Find index of initial head position in the sorted list
        int initialHeadIndex = this.cylinderRequests.indexOf(this.initialPosition);

        // Move the head towards the beginning of the disk
        for (int i = initialHeadIndex - 1; i >= 0; i--) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }

        // Move the head towards the end of the disk
        for (int i = cylinderRequests.size() - 1; i > initialHeadIndex; i--) {
        	totalHeadMovement += Math.abs(this.cylinderRequests.get(i) - currentHeadPosition);
            currentHeadPosition = this.cylinderRequests.get(i);
        }
        // Return the total head movement
        return totalHeadMovement;
}
}

package org.ggp.shared.observer;

public interface Subject
{

	public void addObserver(Observer observer);

	public void notifyObservers(Event event);

}

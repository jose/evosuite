package org.evosuite.ga.metaheuristics.paes.Grid;

import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;

import java.util.*;

/**
 * Leaf node in a tree structured archive in a Pareto archived evolution strategy.
 * Stores the solutions in this area of the grid in a {@link List}.
 *
 * @param <C> the class extending {@link Chromosome}. Objects of this class
 *              are stored in the gridLocation
 */
public class GridLocation <C extends Chromosome> implements GridNodeInterface<C>{
    private List<C> located = new ArrayList<>();
    private Map<FitnessFunction<?>, Double> lowerBounds;
    private Map<FitnessFunction<?>, Double> upperBounds;
    private GridNode<C> parent;

    /**
     * Creates a new GridLocation object with given bounds.
     *
     * @param lowerBounds the minimum values for the objectives in this area of the grid.
     * @param upperBounds the maximum values for the objectives in this area of the grid.
     */
    public GridLocation(Map<FitnessFunction<?>, Double> lowerBounds, Map<FitnessFunction<?>, Double> upperBounds, GridNode<C> parent){
        if(lowerBounds.size() != upperBounds.size())
            throw new IllegalArgumentException("lower and upper bounds must have the same length");
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInBounds(C c){
        for(FitnessFunction<?> ff: this.getObjectives())
            if(FitnessFunction.normalize(c.getFitness(ff)) > this.upperBounds.get(ff) ||
                    FitnessFunction.normalize(c.getFitness(ff)) < this.lowerBounds.get(ff))
                return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int count(){
        return located.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(C c){
        located.add(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(C c){
        located.remove(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(Collection<C> c) {
        located.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<C> getAll() {
        return located;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridLocation<C> mostCrowdedRegion() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridNodeInterface<C> current_mostCrowdedRegion() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridLocation<C> region(C c) {
        return this.isInBounds(c) ? this : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridNodeInterface<C> region(C c, int depth){
        if(depth != 0)
            throw new IllegalArgumentException("Grid has reached leaf");
        return this.isInBounds(c) ? this : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridNodeInterface<C> current_region(C c) {
        return this.isInBounds(c) ? this : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int decide(C candidate, C current) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridLocation<C> recursiveMostCrowdedRegion() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FitnessFunction<?>> getObjectives() {
        return this.upperBounds.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<FitnessFunction<?>, Double> getUpperBounds() {
        return this.upperBounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<FitnessFunction<?>, Double> getLowerBounds() {
        return this.lowerBounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridNode<C> getParent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GridNodeInterface<C>> regions(C c){
        List<GridNodeInterface<C>> result = new ArrayList<>();
        if(this.isInBounds(c))
            result.add(0,null);
        else
            result.add(0,this);
        return result;
    }
}

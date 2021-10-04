/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2020-- Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020-- Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * Tai-e is only for educational and academic purposes,
 * and any form of commercial use is disallowed.
 * Distribution of Tai-e is disallowed without the approval.
 */

package pascal.taie.analysis.dataflow.inter;

import pascal.taie.World;
import pascal.taie.analysis.InterproceduralAnalysis;
import pascal.taie.analysis.graph.icfg.CallEdge;
import pascal.taie.analysis.graph.icfg.CallToReturnEdge;
import pascal.taie.analysis.graph.icfg.ICFG;
import pascal.taie.analysis.graph.icfg.ICFGBuilder;
import pascal.taie.analysis.graph.icfg.ICFGEdge;
import pascal.taie.analysis.graph.icfg.NormalEdge;
import pascal.taie.analysis.graph.icfg.ReturnEdge;
import pascal.taie.config.AnalysisConfig;

public abstract class AbstractInterDataflowAnalysis<Method, Node, Fact>
        extends InterproceduralAnalysis
        implements InterDataflowAnalysis<Node, Fact> {

    protected ICFG<Method, Node> icfg;

    protected InterSolver<Method, Node, Fact> solver;

    public AbstractInterDataflowAnalysis(AnalysisConfig config) {
        super(config);
    }

    @Override
    public boolean transferNode(Node node, Fact in, Fact out) {
        if (icfg.isCallSite(node)) {
            return transferCallNode(node, in, out);
        } else {
            return transferNonCallNode(node, in, out);
        }
    }

    protected abstract boolean transferCallNode(Node node, Fact in, Fact out);

    protected abstract boolean transferNonCallNode(Node node, Fact in, Fact out);

    @Override
    public void transferEdge(
            ICFGEdge<Node> edge, Fact out, Fact edgeFact) {
        if (edge instanceof NormalEdge) {
            transferNormalEdge((NormalEdge<Node>) edge, out, edgeFact);
        } else if (edge instanceof CallToReturnEdge) {
            transferCallToReturnEdge((CallToReturnEdge<Node>) edge, out, edgeFact);
        } else if (edge instanceof CallEdge) {
            transferCallEdge((CallEdge<Node>) edge, out, edgeFact);
        } else {
            transferReturnEdge((ReturnEdge<Node>) edge, out, edgeFact);
        }
    }

    protected abstract void transferNormalEdge(NormalEdge<Node> edge, Fact out, Fact edgeFact);

    protected abstract void transferCallToReturnEdge(CallToReturnEdge<Node> edge, Fact out, Fact edgeFact);

    protected abstract void transferCallEdge(CallEdge<Node> edge, Fact callSiteOut, Fact edgeFact);

    protected abstract void transferReturnEdge(ReturnEdge<Node> edge, Fact returnOut, Fact edgeFact);

    @Override
    public Object analyze() {
        icfg = World.getResult(ICFGBuilder.ID);
        solver = new InterSolver<>(this, icfg);
        return solver.solve();
    }
}

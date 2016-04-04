/**
 * Contains classes describing items on a printed circuit board and the board itself.
 *
 * @(#)package.html 1.60 98/01/27
 * 
 * Copyright 1993-1997 Sun Microsystems, Inc. 901 San Antonio Road, 
 * Palo Alto, California, 94303, U.S.A.  All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 * 
 * CopyrightVersion 1.2
 * 
 * <p>The class LayeredBoard implements elementary functionality for the layout of 2-dimensional items on a board. The board may have several layers.  It allows to insert, change, or delete items, or to check, if inserting would result in violations (overlaps) with other items. For fast checking, picking  and changing items  the class contains a 2-dimensional search tree, whose entries point into the list of items on the board.  The search tree  is decoupled from the items it is pointing to and may even point into several different databases at the same time.</p>
 * <p>LayeredBoard contains also a list of components, a library of packages and padstacks, and a collection of rules and restrictions, which must be respected by the items on the board.</p>
 * <p>The class RoutingBoard derived from BasicBoard contains higher level functionality, such as shoving items and pulling tight traces, and autorouting incomplete connections.</p>
 * <p>The base classes of all physical items on the board is the abstract class  Item. Items must implement the interfaces ShapeTree.Storable and Drawable, so that they can be stored in a search ree and painted onto a graphics screen. Additionally an item contains a pointer to the physical board the item is on, an id number and a list of numbers of nets it belongs to, which may be empty.</p>
 * <p>Classes derived from Item are currently Trace, DrillItem , ObstacleArea and BoardOutline. The class ObstacleArea describe areas on the board, which may be conduction areas or obstacle areas for traces or vias. The abstact class DrillItem describes items with a layer range, a centre point and a (convex) shape on each  layer. It has the two implementations Pin and Via. Pins belong to a component, and its shapes are defined by the padstack of the corresponding pin of the components library package. The shapes of a Via are defined directly by a library padstack. The class abstract Trace is used for paths connecting  drill items and eventual conduction areas. The only implementation of the class Trace is currently the class PolylineTrace.  It adds the concrete description of the geometric path as a Polyline to the class Trace. The reason, why Polylines and not Polygons are used to implement the paths of non-curved tracess, has to do with numerical exactness and performance, as described in the package geometry.planar.</p>
 * <p>Items, which may be electrically connected must implement the interface Connectable. Connectable Items are currently Pins, Vias, Traces and ConductionAreas.</p>
 */
package net.freerouting.freeroute.board;

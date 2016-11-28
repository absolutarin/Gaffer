/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.gaffer.operation.impl.get;

import org.junit.Test;
import uk.gov.gchq.gaffer.data.elementdefinition.view.View;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.operation.GetOperation;
import uk.gov.gchq.gaffer.operation.GetOperation.SeedMatchingType;
import uk.gov.gchq.gaffer.operation.OperationTest;
import uk.gov.gchq.gaffer.operation.data.EdgeSeed;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GetEdgesBySeedTest implements OperationTest {
    private static final JSONSerialiser serialiser = new JSONSerialiser();

    @Test
    public void shouldSetSeedMatchingTypeToEquals() {
        // Given
        final EdgeSeed seed1 = new EdgeSeed("source1", "destination1", true);

        // When
        final GetEdges op = new GetEdges.Builder().seeds(Collections.singletonList(seed1))
                                                  .seedMatching(SeedMatchingType.EQUAL)
                                                  .build();

        // Then
        assertEquals(GetOperation.SeedMatchingType.EQUAL, op.getSeedMatching());
    }

    @Test
    @Override
    public void shouldSerialiseAndDeserialiseOperation() throws SerialisationException {
        // Given
        final EdgeSeed seed1 = new EdgeSeed("source1", "destination1", true);
        final EdgeSeed seed2 = new EdgeSeed("source2", "destination2", true);
        final GetEdges op = new GetEdges(Arrays.asList(seed1, seed2));

        // When
        byte[] json = serialiser.serialise(op, true);
        final GetEdges deserialisedOp = serialiser.deserialise(json, GetEdges.class);

        // Then
        final Iterator itr = deserialisedOp.getSeeds().iterator();
        assertEquals(seed1, itr.next());
        assertEquals(seed2, itr.next());
        assertFalse(itr.hasNext());
    }

    @Test
    @Override
    public void builderShouldCreatePopulatedOperation() {
        EdgeSeed seed = new EdgeSeed("A", "B", true);
        GetEdges getEdgesBySeed = new GetEdges.Builder()
                .addSeed(seed)
                .includeEdges(GetOperation.IncludeEdgeType.DIRECTED)
                .inOutType(GetOperation.IncludeIncomingOutgoingType.OUTGOING)
                .option("testOption", "true")
                .populateProperties(true)

                .view(new View.Builder()
                        .edge("testEdgeGroup")
                        .build())
                .build();
        assertTrue(getEdgesBySeed.isPopulateProperties());
        assertNotNull(getEdgesBySeed.getView());
        assertEquals("true", getEdgesBySeed.getOption("testOption"));
        assertEquals(GetOperation.IncludeEdgeType.DIRECTED, getEdgesBySeed.getIncludeEdges());
        assertEquals(GetOperation.IncludeIncomingOutgoingType.OUTGOING, getEdgesBySeed.getIncludeIncomingOutGoing());
        assertEquals(seed, getEdgesBySeed.getInput().iterator().next());
    }
}

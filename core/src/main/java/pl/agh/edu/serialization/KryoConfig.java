package pl.agh.edu.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.advertisement.AdvertisementCampaign;
import pl.agh.edu.engine.advertisement.AdvertisementType;
import pl.agh.edu.engine.attraction.Attraction;
import pl.agh.edu.engine.attraction.AttractionSize;
import pl.agh.edu.engine.attraction.AttractionState;
import pl.agh.edu.engine.attraction.AttractionType;
import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.engine.bank.Transaction;
import pl.agh.edu.engine.bank.TransactionType;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.client.Client;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.Gender;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.opinion.Opinion;
import pl.agh.edu.engine.opinion.bucket.EmployeesSatisfactionOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.QueueWaitingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomBreakingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomCleaningOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomPriceOpinionBucket;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.IntStream;

public class KryoConfig {

    public KryoConfig(Kryo kryo) {
        kryo.setReferences(true);

        kryo.register(BigDecimal.class);
        kryo.register(LocalDate.class);
        kryo.register(LocalDateTime.class);
        kryo.register(Pair.class);
        kryo.register(YearMonth.class);
        kryo.register(Year.class);
        kryo.register(Duration.class);


        registerAdvertisementCampaign(kryo);
        registerAttraction(kryo);

        //bank
        registerBankDaoClasses(kryo);

        //languageString
        registerLanguageString(kryo);

        //calendar
        kryo.register(CalendarEvent.class);

        //
        registerOpinion(kryo);

        //client.generation
        registerClientGroup(kryo);



    }

    private void registerAdvertisementCampaign(Kryo kryo){
        kryo.register(AdvertisementType.class);
        kryo.register(AdvertisementCampaign.class, new Serializer<AdvertisementCampaign>() {
            @Override
            public void write(Kryo kryo, Output output, AdvertisementCampaign object) {
                kryo.writeObject(output,object.advertisementData().type());
                kryo.writeObject(output,object.startDate());
                kryo.writeObject(output,object.endDate());
            }

            @Override
            public AdvertisementCampaign read(Kryo kryo, Input input, Class<? extends AdvertisementCampaign> type) {
                return new AdvertisementCampaign(
                        JSONAdvertisementDataLoader.advertisementData.get(kryo.readObject(input, AdvertisementType.class)),
                        kryo.readObject(input, LocalDate.class),
                        kryo.readObject(input, LocalDate.class)
                );
            }
        });
    }

    private void registerAttraction(Kryo kryo) {
        kryo.register(AttractionType.class);
        kryo.register(AttractionSize.class);
        kryo.register(AttractionState.class);
        kryo.register(Attraction.class, new Serializer<Attraction>() {
            @Override
            public void write(Kryo kryo, Output output, Attraction object) {
                kryo.writeObject(output, object.type);
                kryo.writeObject(output, object.getSize());
                kryo.writeObject(output, object.getState());
            }

            @Override
            public Attraction read(Kryo kryo, Input input, Class<? extends Attraction> type) {
                Attraction attraction = new Attraction(
                        kryo.readObject(input, AttractionType.class),
                        kryo.readObject(input, AttractionSize.class)
                );
                attraction.setState(kryo.readObject(input, AttractionState.class));
                return attraction;
            }
        });
    }

    private void registerBankDaoClasses(Kryo kryo) {

        kryo.register(Credit.class, new Serializer<Credit>() {
            @Override
            public void write(Kryo kryo, Output output, Credit object) {
                kryo.writeObject(output, object.value);
                kryo.writeObject(output, object.lengthInMonths);
                kryo.writeObject(output, object.interestRate);
                kryo.writeObject(output, object.takeOutDate);
            }

            @Override
            public Credit read(Kryo kryo, Input input, Class<? extends Credit> type) {
                return new Credit(
                        kryo.readObject(input, BigDecimal.class),
                        kryo.readObject(input, Long.class),
                        kryo.readObject(input, BigDecimal.class),
                        kryo.readObject(input, LocalDate.class)
                        );
            }
        });
        kryo.register(TransactionType.class);
        kryo.register(Transaction.class);
        kryo.register(BankAccountDetails.class);
        kryo.register(BankData.class);
    }

    public void registerLanguageString(Kryo kryo){

        kryo.register(LanguageString.class, new Serializer<LanguageString>() {
            @Override
            public void write(Kryo kryo, Output output, LanguageString object) {
                kryo.writeObject(output, object.path);
                kryo.writeObject(output, object.replacementsList, listSerializer(Pair.class));

            }

            @Override
            public LanguageString read(Kryo kryo, Input input, Class<? extends LanguageString> type) {
                return new LanguageString(
                        kryo.readObject(input, String.class),
                        kryo.readObject(input, List.class, listSerializer(Pair.class))
                );
            }
        });
    }

    public void registerOpinion(Kryo kryo) {
        kryo.register(RoomCleaningOpinionBucket.class, new Serializer<RoomCleaningOpinionBucket>() {
            @Override
            public void write(Kryo kryo, Output output, RoomCleaningOpinionBucket object) {
                kryo.writeObject(output,object.weight);
                kryo.writeObject(output,getPrivateFieldValue(object,"numberOfNights",Integer.class));
                kryo.writeObject(output,getPrivateFieldValue(object,"gotCleanRoom",Boolean.class));
                kryo.writeObject(output,getPrivateFieldValue(object,"cleanRoomCounter",Integer.class));

            }

            @Override
            public RoomCleaningOpinionBucket read(Kryo kryo, Input input, Class<? extends RoomCleaningOpinionBucket> type) {
                RoomCleaningOpinionBucket roomCleaningOpinionBucket = new RoomCleaningOpinionBucket(
                        kryo.readObject(input, Integer.class),
                        kryo.readObject(input, Integer.class)
                );
                setPrivateFieldValue(roomCleaningOpinionBucket,"gotCleanRoom",kryo.readObject(input, Boolean.class));
                setPrivateFieldValue(roomCleaningOpinionBucket,"cleanRoomCounter",kryo.readObject(input, Integer.class));

                return roomCleaningOpinionBucket;
            }
        });

        kryo.register(RoomBreakingOpinionBucket.class, new Serializer<RoomBreakingOpinionBucket>() {
            @Override
            public void write(Kryo kryo, Output output, RoomBreakingOpinionBucket object) {
                kryo.writeObject(output,object.weight);
                kryo.writeObject(output,getPrivateFieldValue(object,"gotBrokenRoom",Boolean.class));
                kryo.writeObject(output,getPrivateFieldValue(object,"roomBroke",Boolean.class));
                kryo.writeObject(output,getPrivateFieldValue(object,"repaired",Boolean.class));

            }

            @Override
            public RoomBreakingOpinionBucket read(Kryo kryo, Input input, Class<? extends RoomBreakingOpinionBucket> type) {
                RoomBreakingOpinionBucket roomBreakingOpinionBucket = new RoomBreakingOpinionBucket(kryo.readObject(input, Integer.class));

                roomBreakingOpinionBucket.setGotBrokenRoom(kryo.readObject(input, Boolean.class));
                setPrivateFieldValue(roomBreakingOpinionBucket,"roomBroke",kryo.readObject(input, Boolean.class));
                setPrivateFieldValue(roomBreakingOpinionBucket,"repaired",kryo.readObject(input, Boolean.class));

                return roomBreakingOpinionBucket;
            }
        });

        kryo.register(RoomPriceOpinionBucket.class, new Serializer<RoomPriceOpinionBucket>() {
            @Override
            public void write(Kryo kryo, Output output, RoomPriceOpinionBucket object) {
                kryo.writeObject(output,object.weight);
                kryo.writeObject(output,getPrivateFieldValue(object,"maxPrice",BigDecimal.class));
                kryo.writeObjectOrNull(output,getPrivateFieldValue(object,"offeredPrice",BigDecimal.class),BigDecimal.class);

            }

            @Override
            public RoomPriceOpinionBucket read(Kryo kryo, Input input, Class<? extends RoomPriceOpinionBucket> type) {
                RoomPriceOpinionBucket roomBreakingOpinionBucket = new RoomPriceOpinionBucket(
                        kryo.readObject(input, Integer.class),
                        kryo.readObject(input, BigDecimal.class));

                roomBreakingOpinionBucket.setPrices(kryo.readObjectOrNull(input, BigDecimal.class));

                return roomBreakingOpinionBucket;
            }
        });

        kryo.register(QueueWaitingOpinionBucket.class, new Serializer<QueueWaitingOpinionBucket>() {
            @Override
            public void write(Kryo kryo, Output output, QueueWaitingOpinionBucket object) {
                kryo.writeObject(output,object.weight);
                kryo.writeObject(output,getPrivateFieldValue(object,"maxWaitingTime",Duration.class));
                kryo.writeObjectOrNull(output,getPrivateFieldValue(object,"startDate",LocalDateTime.class),LocalDateTime.class);
                kryo.writeObjectOrNull(output,getPrivateFieldValue(object,"endDate",LocalDateTime.class),LocalDateTime.class);

            }

            @Override
            public QueueWaitingOpinionBucket read(Kryo kryo, Input input, Class<? extends QueueWaitingOpinionBucket> type) {
                QueueWaitingOpinionBucket queueWaitingOpinionBucket = new QueueWaitingOpinionBucket(
                        kryo.readObject(input, Integer.class),
                        kryo.readObject(input, Duration.class));

                setPrivateFieldValue(queueWaitingOpinionBucket,"startDate",kryo.readObjectOrNull(input, LocalDateTime.class));
                setPrivateFieldValue(queueWaitingOpinionBucket,"endDate",kryo.readObjectOrNull(input, LocalDateTime.class));

                return queueWaitingOpinionBucket;
            }
        });

        kryo.register(EmployeesSatisfactionOpinionBucket.class, new Serializer<EmployeesSatisfactionOpinionBucket>() {
            @Override
            public void write(Kryo kryo, Output output, EmployeesSatisfactionOpinionBucket object) {
                kryo.writeObject(output,object.weight);
                kryo.writeObject(output,getPrivateFieldValue(object,"satisfactions",List.class), listSerializer(BigDecimal.class));
            }

            @Override
            public EmployeesSatisfactionOpinionBucket read(Kryo kryo, Input input, Class<? extends EmployeesSatisfactionOpinionBucket> type) {
                EmployeesSatisfactionOpinionBucket employeesSatisfactionOpinionBucket = new EmployeesSatisfactionOpinionBucket(
                        kryo.readObject(input, Integer.class));

                List<BigDecimal> satisfactions = kryo.readObject(input,List.class,listSerializer(BigDecimal.class));

                satisfactions.forEach(employeesSatisfactionOpinionBucket::addSatisfaction);

                return employeesSatisfactionOpinionBucket;
            }
        });


        kryo.register(Opinion.class, new Serializer<Opinion>() {
            @Override
            public void write(Kryo kryo, Output output, Opinion object) {
                kryo.writeObject(output, object.roomCleaning);
                kryo.writeObject(output, object.roomBreaking);
                kryo.writeObject(output, object.roomPrice);
                kryo.writeObject(output, object.queueWaiting);
                kryo.writeObject(output, object.employeesSatisfaction);
                kryo.writeObject(output, getPrivateFieldValue(object,"clientGroupGotRoom",Boolean.class));
                kryo.writeObject(output, getPrivateFieldValue(object,"clientSteppedOutOfQueue",Boolean.class));

            }

            @Override
            public Opinion read(Kryo kryo, Input input, Class type) {
                return new Opinion(
                        kryo.readObject(input, RoomCleaningOpinionBucket.class),
                        kryo.readObject(input, RoomBreakingOpinionBucket.class),
                        kryo.readObject(input, RoomPriceOpinionBucket.class),
                        kryo.readObject(input, QueueWaitingOpinionBucket.class),
                        kryo.readObject(input, EmployeesSatisfactionOpinionBucket.class),
                        kryo.readObject(input, Boolean.class),
                        kryo.readObject(input, Boolean.class)
                        );
            }
        });
    }

    public void registerClientGroup(Kryo kryo) {
        kryo.register(Client.class);
        kryo.register(HotelVisitPurpose.class);
        kryo.register(RoomRank.class);
        kryo.register(Gender.class);

        kryo.register(ClientGroup.class, new Serializer<ClientGroup>() {
            @Override
            public void write(Kryo kryo, Output output, ClientGroup object) {
                kryo.writeObject(output, object.getMembers(), listSerializer(Client.class));
                kryo.writeObject(output, object.getHotelVisitPurpose());
                kryo.writeObject(output, object.getDesiredPricePerNight());
                kryo.writeObject(output, object.getDesiredRoomRank());
                kryo.writeObject(output, object.getMaxWaitingTime());
                kryo.writeObject(output, object.getNumberOfNights());
                kryo.writeObject(output, object.opinion);
            }

            @Override
            public ClientGroup read(Kryo kryo, Input input, Class<? extends ClientGroup> type) {
                ClientGroup clientGroup = new ClientGroup.Builder()
                        .members(kryo.readObject(input, List.class, listSerializer(Client.class)))
                        .hotelVisitPurpose(kryo.readObject(input, HotelVisitPurpose.class))
                        .desiredPricePerNight(kryo.readObject(input, BigDecimal.class))
                        .desiredRoomRank(kryo.readObject(input, RoomRank.class))
                        .maxWaitingTime(kryo.readObject(input, Duration.class))
                        .numberOfNights(kryo.readObject(input, Integer.class))
                        .build();

                setPrivateFieldValue(clientGroup,"opinion", kryo.readObject(input, Opinion.class));

                return clientGroup;
            }
        });

    }





    private <T> Serializer<List<T>> listSerializer(Class<T> clazz){
        return new Serializer<>() {

            @Override
            public void write(Kryo kryo, Output output, List<T> object) {
                kryo.writeObject(output, object.size());
                object.forEach(pair -> kryo.writeObject(output, pair));
            }

            @Override
            public List<T> read(Kryo kryo, Input input, Class<? extends List<T>> type) {
                int size = kryo.readObject(input, Integer.class);
                return IntStream.range(0, size).mapToObj(i -> kryo.readObject(input, clazz)).toList();
            }
        };
    }

    private static  <T> T getPrivateFieldValue(Object object, String fieldName, Class<T> clazz) {
        Field f; //NoSuchFieldException
        try {
            f = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        f.setAccessible(true);
        try {
            return clazz.cast(f.get(object));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void  setPrivateFieldValue(Object object, String fieldName, Object valueTobeSet) {
        Field field;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        try {
            field.set(object, valueTobeSet);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        RoomCleaningOpinionBucket roomCleaningOpinionBucket = new RoomCleaningOpinionBucket(3,5);
        System.out.println(getPrivateFieldValue(roomCleaningOpinionBucket,"numberOfNights", Integer.class));
    }
}

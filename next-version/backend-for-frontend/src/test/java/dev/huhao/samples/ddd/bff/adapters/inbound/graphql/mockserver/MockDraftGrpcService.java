package dev.huhao.samples.ddd.bff.adapters.inbound.graphql.mockserver;

import dev.huhao.samples.ddd.protobuf.blog.Draft.DraftDto;
import dev.huhao.samples.ddd.protobuf.blog.Draft.GetDraftRequest;
import dev.huhao.samples.ddd.protobuf.blog.DraftServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
class MockDraftGrpcService extends DraftServiceGrpc.DraftServiceImplBase {
    @Override
    public void getDraft(GetDraftRequest request, StreamObserver<DraftDto> responseObserver) {
        responseObserver.onNext(DraftDto.newBuilder()
                .setBlogId("95c06541-d58c-482e-9cff-5a0f2a696aac")
                .setTitle("Hello")
                .setBody("Hello")
                .setAuthorId("3e490af5-085b-48d7-9593-0ceed5fae7af")
                .setCreatedAt("2020-05-18T13:57:09.635Z")
                .setSavedAt("2020-05-18T13:57:09.635Z")
                .build());
        responseObserver.onCompleted();
    }
}

package dev.huhao.samples.ddd.blogservice.adapters.inbound.grpc.blog;

import dev.huhao.samples.ddd.blogservice.application.usecase.EditBlogUseCase;
import dev.huhao.samples.ddd.blogservice.application.usecase.QueryDraftUseCase;
import dev.huhao.samples.ddd.blogservice.domain.contexts.blogcontext.blog.Blog;
import dev.huhao.samples.ddd.protobuf.blog.Draft.CreateDraftRequest;
import dev.huhao.samples.ddd.protobuf.blog.Draft.DraftDto;
import dev.huhao.samples.ddd.protobuf.blog.Draft.GetDraftRequest;
import dev.huhao.samples.ddd.protobuf.blog.Draft.SaveDraftRequest;
import dev.huhao.samples.ddd.protobuf.blog.DraftServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class DraftGrpcService extends DraftServiceGrpc.DraftServiceImplBase {

    private final EditBlogUseCase editBlogUseCase;
    private final QueryDraftUseCase queryDraftUseCase;

    public DraftGrpcService(EditBlogUseCase editBlogUseCase, QueryDraftUseCase queryDraftUseCase) {
        this.editBlogUseCase = editBlogUseCase;
        this.queryDraftUseCase = queryDraftUseCase;
    }

    @Override
    public void createDraft(CreateDraftRequest request, StreamObserver<DraftDto> responseObserver) {
        if (request.getAuthorId() == null || request.getAuthorId().trim().isEmpty()) {
            throw new IllegalArgumentException("the blog must have author");
        }

        Blog blog = editBlogUseCase
                .createDraft(request.getTitle(), request.getBody(), UUID.fromString(request.getAuthorId()));

        responseObserver.onNext(buildDraftDto(blog));
        responseObserver.onCompleted();
    }

    @Override
    public void getDraft(GetDraftRequest request, StreamObserver<DraftDto> responseObserver) {
        Blog blog = queryDraftUseCase.getByBlogId(UUID.fromString(request.getBlogId()));
        responseObserver.onNext(buildDraftDto(blog));
        responseObserver.onCompleted();
    }

    @Override
    public void updateDraft(SaveDraftRequest request, StreamObserver<DraftDto> responseObserver) {
        Blog blog =
                editBlogUseCase.updateDraft(UUID.fromString(request.getBlogId()), request.getTitle(), request.getBody());
        responseObserver.onNext(buildDraftDto(blog));
        responseObserver.onCompleted();
    }

    private DraftDto buildDraftDto(Blog blog) {
        return DraftDto.newBuilder()
                .setBlogId(blog.getId().toString())
                .setTitle(blog.getDraft().getTitle())
                .setBody(blog.getDraft().getBody())
                .setAuthorId(blog.getAuthorId().toString())
                .setCreatedAt(blog.getCreatedAt().toString())
                .setSavedAt(blog.getDraft().getSavedAt().toString())
                .build();
    }
}
